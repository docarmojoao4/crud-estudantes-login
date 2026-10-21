package br.com.estudantes.dao;

import br.com.estudantes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Substitui o antigo UsuarioDAO em JDBC.
 * cadastrar/editar -> save(), apagar -> deleteById(), buscarPorID -> findById().
 */
@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Integer> {

    // SELECT ... FROM Usuarios WHERE login = ?
    List<Usuario> findByLogin(String login);
}
