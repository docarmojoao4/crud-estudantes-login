const API_BASE = '/api';

async function request(path, options = {}) {
    const resposta = await fetch(API_BASE + path, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });

    if (resposta.status === 204) return null;

    let corpo = null;
    try {
        corpo = await resposta.json();
    } catch (e) {}

    if (!resposta.ok) {
        const erro = new Error(corpo?.mensagem || `Erro inesperado (${resposta.status})`);
        erro.status = resposta.status;
        throw erro;
    }
    return corpo;
}

const json = (dados) => JSON.stringify(dados);

const Api = {
    login: (login, senha) => request('/auth/login', { method: 'POST', body: json({ login, senha }) }),
    cadastrarUsuario: (usuario) => request('/usuarios', { method: 'POST', body: json(usuario) }),
    editarUsuario: (id, usuario) => request(`/usuarios/${id}`, { method: 'PUT', body: json(usuario) }),
    apagarUsuario: (id) => request(`/usuarios/${id}`, { method: 'DELETE' }),

    listarEstudantes: () => request('/estudantes'),
    buscarEstudantes: (nome) => request(`/estudantes/busca?nome=${encodeURIComponent(nome)}`),
    cadastrarEstudante: (dados) => request('/estudantes', { method: 'POST', body: json(dados) }),
    editarEstudante: (id, dados) => request(`/estudantes/${id}`, { method: 'PUT', body: json(dados) }),
    apagarEstudante: (id) => request(`/estudantes/${id}`, { method: 'DELETE' })
};
