const $ = (seletor) => document.querySelector(seletor);


const isoParaBr = (iso) => iso.split('-').reverse().join('/');
const brParaIso = (br) => br.split('/').reverse().join('-');

let toastTimer;
function toast(mensagem, ok = false) {
    const el = $('#toast');
    el.textContent = mensagem;
    el.className = ok ? 'toast ok' : 'toast';
    el.hidden = false;
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => (el.hidden = true), 4000);
}


let usuario = null;
try {
    usuario = JSON.parse(sessionStorage.getItem('usuario'));
} catch (e) {
    usuario = null;
}
let estudanteEmEdicao = null;

function definirUsuario(novo) {
    usuario = novo;
    if (novo) sessionStorage.setItem('usuario', JSON.stringify(novo));
    else sessionStorage.removeItem('usuario');
    mostrarTela();
}

function mostrarTela() {
    $('#view-auth').hidden = !!usuario;
    $('#view-app').hidden = !usuario;
    if (usuario) {
        $('#usuario-nome').textContent = usuario.nome;
        carregarEstudantes($('#busca-nome').value);
    }
}


document.querySelectorAll('.tab').forEach((aba) => {
    aba.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach((a) => a.classList.toggle('active', a === aba));
        $('#form-login').hidden = aba.dataset.tab !== 'login';
        $('#form-registro').hidden = aba.dataset.tab !== 'registro';
    });
});

$('#form-login').addEventListener('submit', async (e) => {
    e.preventDefault();
    const f = new FormData(e.target);
    try {
        definirUsuario(await Api.login(f.get('login'), f.get('senha')));
        e.target.reset();
    } catch (erro) {
        toast(erro.message);
    }
});

$('#form-registro').addEventListener('submit', async (e) => {
    e.preventDefault();
    const f = new FormData(e.target);
    const novo = { nome: f.get('nome'), login: f.get('login'), senha: f.get('senha') };
    try {
        await Api.cadastrarUsuario(novo);
        definirUsuario(await Api.login(novo.login, novo.senha));
        e.target.reset();
        toast('Conta criada com sucesso!', true);
    } catch (erro) {
        toast(erro.message);
    }
});

$('#btn-sair').addEventListener('click', () => definirUsuario(null));


async function carregarEstudantes(nome = '') {
    try {
        const termo = nome.trim();
        const lista = termo ? await Api.buscarEstudantes(termo) : await Api.listarEstudantes();
        renderizarEstudantes(lista);
    } catch (erro) {
        if (erro.status === 404) renderizarEstudantes([]); 
        else toast(erro.message);
    }
}

function celula(texto) {
    const td = document.createElement('td');
    td.textContent = texto ?? '';
    return td;
}

function botao(texto, classe, aoClicar) {
    const b = document.createElement('button');
    b.type = 'button';
    b.className = `btn sm ${classe}`;
    b.textContent = texto;
    b.addEventListener('click', aoClicar);
    return b;
}

function renderizarEstudantes(lista) {
    const corpo = $('#tabela-estudantes');
    corpo.replaceChildren();
    $('#vazio').hidden = lista.length > 0;

    lista.forEach((est) => {
        const tr = document.createElement('tr');
        tr.append(
            celula(est.id),
            celula(est.nome),
            celula(est.cpf),
            celula(est.idade),
            celula(est.dataNascimento),
            celula(est.usuario?.nome),
            celula(est.ultimaAlteracao ?? '—')
        );

        const tdAcoes = document.createElement('td');
        const acoes = document.createElement('div');
        acoes.className = 'acoes';
        acoes.append(
            botao('Editar', '', () => abrirFormEstudante(est)),
            botao('Excluir', 'danger', () => excluirEstudante(est))
        );
        tdAcoes.append(acoes);
        tr.append(tdAcoes);
        corpo.append(tr);
    });
}

$('#form-busca').addEventListener('submit', (e) => {
    e.preventDefault();
    carregarEstudantes($('#busca-nome').value);
});

$('#btn-limpar').addEventListener('click', () => {
    $('#busca-nome').value = '';
    carregarEstudantes();
});


function abrirFormEstudante(est = null) {
    estudanteEmEdicao = est;
    const form = $('#form-estudante');
    $('#dlg-estudante-titulo').textContent = est ? `Editar estudante #${est.id}` : 'Novo estudante';
    form.nome.value = est?.nome ?? '';
    form.cpf.value = est?.cpf ?? '';
    form.dataNascimento.value = est ? brParaIso(est.dataNascimento) : '';
    form.dataNascimento.max = new Date().toISOString().split('T')[0];
    $('#dlg-estudante').showModal();
}

$('#btn-novo').addEventListener('click', () => abrirFormEstudante());

$('#form-estudante').addEventListener('submit', async (e) => {
    e.preventDefault();
    const f = new FormData(e.target);
    const dados = {
        nome: f.get('nome'),
        cpf: f.get('cpf'),
        dataNascimento: isoParaBr(f.get('dataNascimento')),
        usuarioId: usuario.id
    };
    try {
        if (estudanteEmEdicao) await Api.editarEstudante(estudanteEmEdicao.id, dados);
        else await Api.cadastrarEstudante(dados);
        $('#dlg-estudante').close();
        toast(estudanteEmEdicao ? 'Estudante atualizado!' : 'Estudante cadastrado!', true);
        carregarEstudantes($('#busca-nome').value);
    } catch (erro) {
        toast(erro.message);
    }
});

async function excluirEstudante(est) {
    if (!confirm(`Excluir o estudante "${est.nome}"?`)) return;
    try {
        await Api.apagarEstudante(est.id);
        toast('Estudante excluído!', true);
        carregarEstudantes($('#busca-nome').value);
    } catch (erro) {
        toast(erro.message);
    }
}

$('#btn-conta').addEventListener('click', () => {
    const form = $('#form-conta');
    form.nome.value = usuario.nome;
    form.login.value = usuario.login;
    form.senha.value = '';
    $('#dlg-conta').showModal();
});

$('#form-conta').addEventListener('submit', async (e) => {
    e.preventDefault();
    const f = new FormData(e.target);
    const dados = { nome: f.get('nome'), login: f.get('login'), senha: f.get('senha') || null };
    try {
        const atualizado = await Api.editarUsuario(usuario.id, dados);
        $('#dlg-conta').close();
        definirUsuario(atualizado);
        toast('Conta atualizada!', true);
    } catch (erro) {
        toast(erro.message);
    }
});

$('#btn-apagar-conta').addEventListener('click', async () => {
    if (!confirm('Apagar sua conta definitivamente?')) return;
    try {
        await Api.apagarUsuario(usuario.id);
        $('#dlg-conta').close();
        definirUsuario(null);
        toast('Conta apagada.', true);
    } catch (erro) {
        toast(erro.message); 
    }
});

document.querySelectorAll('[data-close]').forEach((btn) => {
    btn.addEventListener('click', () => btn.closest('dialog').close());
});

mostrarTela();
