package br.com.estudantes.service;

import br.com.estudantes.dao.EstudanteDAO;
import br.com.estudantes.exception.RecursoNaoEncontradoException;
import br.com.estudantes.exception.RegraNegocioException;
import br.com.estudantes.model.Estudante;
import br.com.estudantes.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

@Service
public class EstudanteService {

    private final EstudanteDAO estudanteDAO;

    public EstudanteService(EstudanteDAO estudanteDAO) {
        this.estudanteDAO = estudanteDAO;
    }

    @Transactional
    public Estudante cadastrar(Estudante estudante, Usuario usuario) {
        estudante.setId(null);
        estudante.setIdade(calcularIdade(estudante.getDataNascimento()));
        estudante.setUsuario(usuario);
        return estudanteDAO.save(estudante);
    }

    @Transactional
    public Estudante editar(Estudante estudante, Usuario usuario) {
        estudante.setIdade(calcularIdade(estudante.getDataNascimento()));
        estudante.setUsuario(usuario);
        return estudanteDAO.save(estudante);
    }

    @Transactional
    public void apagar(int id) {
        buscarPorId(id);
        estudanteDAO.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Estudante buscarPorId(int id) {
        return estudanteDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("ID não encontrado!"));
    }

    @Transactional(readOnly = true)
    public List<Estudante> buscarPorNome(String nome) {
        List<Estudante> estudantes = estudanteDAO.findByNomeContainingIgnoreCaseOrderByNome(nome);
        if (estudantes.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nome não encontrado!");
        }
        return estudantes;
    }

    @Transactional(readOnly = true)
    public List<Estudante> listar() {
        return estudanteDAO.findAll();
    }

    /** Regras que antes estavam duplicadas em cadastrar() e editar(). */
    private int calcularIdade(LocalDate dataNasc) {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        if (dataNasc.isAfter(hoje)) {
            throw new RegraNegocioException("A data de nascimento do estudante não pode ser futura!");
        }
        int idade = Period.between(dataNasc, hoje).getYears();
        if (idade < 6) {
            throw new RegraNegocioException("O estudante deve ter no mínimo 6 anos para que possa ser matriculado!");
        }
        return idade;
    }
}
