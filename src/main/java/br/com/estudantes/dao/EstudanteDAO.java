package br.com.estudantes.dao;

import br.com.estudantes.model.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EstudanteDAO extends JpaRepository<Estudante, Integer> {
    List<Estudante> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
