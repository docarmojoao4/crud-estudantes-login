package br.com.estudantes.service;

import br.com.estudantes.dao.UsuarioDAO;
import br.com.estudantes.exception.CredenciaisInvalidasException;
import br.com.estudantes.exception.RecursoNaoEncontradoException;
import br.com.estudantes.exception.RegraNegocioException;
import br.com.estudantes.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Transactional
    public Usuario cadastrar(Usuario usuario) {
        verificarSenha(usuario.getSenha());
        usuario.setId(null);
        return usuarioDAO.save(usuario);
    }

    @Transactional
    public Usuario editar(Usuario usuario) {
        buscarPorId(usuario.getId());
        return usuarioDAO.save(usuario);
    }

    @Transactional
    public void apagar(int id) {
        buscarPorId(id);
        usuarioDAO.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Usuario login(String login, String senha) {
        List<Usuario> usuarios = usuarioDAO.findByLogin(login);
        return usuarios.stream()
                .filter(u -> u.getLogin().equals(login) && u.getSenha().equals(senha))
                .findFirst()
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário ou senha incorretos!"));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(int id) {
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado!"));
    }

    public boolean verificarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new RegraNegocioException("Senha inválida! Defina uma senha contendo pelo menos 8 caracteres!");
        }
        if (senha.replaceAll("[A-Z]", "").equals(senha)) {
            throw new RegraNegocioException("Senha inválida! Defina uma senha com pelo menos uma letra maiúscula!");
        }
        if (senha.replaceAll("\\d", "").equals(senha)) {
            throw new RegraNegocioException("Senha inválida! Defina uma senha com números e letras!");
        }
        if (senha.replaceAll("[a-zA-Z]", "").equals(senha)) {
            throw new RegraNegocioException("Senha inválida! Contém somente números!");
        }
        if (senha.replaceAll("[^a-zA-Z0-9]", "").equals(senha)) {
            throw new RegraNegocioException("Senha inválida! Defina uma senha com pelo menos um caracter especial!");
        }
        return true;
    }
}
