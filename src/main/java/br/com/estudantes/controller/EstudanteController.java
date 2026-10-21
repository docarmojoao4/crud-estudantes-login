package br.com.estudantes.controller;

import br.com.estudantes.dto.EstudanteRequest;
import br.com.estudantes.model.Estudante;
import br.com.estudantes.model.Usuario;
import br.com.estudantes.service.EstudanteService;
import br.com.estudantes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;
    private final UsuarioService usuarioService;

    public EstudanteController(EstudanteService estudanteService, UsuarioService usuarioService) {
        this.estudanteService = estudanteService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudante cadastrar(@Valid @RequestBody EstudanteRequest request) {
        Usuario usuario = usuarioService.buscarPorId(request.usuarioId());
        Estudante estudante = new Estudante(request.nome(), request.cpf(), request.dataNascimento());
        return estudanteService.cadastrar(estudante, usuario);
    }

    @PutMapping("/{id}")
    public Estudante editar(@PathVariable int id, @Valid @RequestBody EstudanteRequest request) {
        Usuario usuario = usuarioService.buscarPorId(request.usuarioId());
        Estudante estudante = estudanteService.buscarPorId(id);
        estudante.setNome(request.nome());
        estudante.setCpf(request.cpf());
        estudante.setDataNascimento(request.dataNascimento());
        return estudanteService.editar(estudante, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable int id) {
        estudanteService.apagar(id);
    }

    @GetMapping
    public List<Estudante> listar() {
        return estudanteService.listar();
    }

    @GetMapping("/{id}")
    public Estudante buscarPorId(@PathVariable int id) {
        return estudanteService.buscarPorId(id);
    }

    @GetMapping("/busca")
    public List<Estudante> buscarPorNome(@RequestParam String nome) {
        return estudanteService.buscarPorNome(nome);
    }
}
