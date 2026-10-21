package br.com.estudantes.controller;

import br.com.estudantes.model.Usuario;
import br.com.estudantes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrar(@Valid @RequestBody Usuario usuario) {
        return usuarioService.cadastrar(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable int id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario editar(@PathVariable int id, @Valid @RequestBody Usuario dados) {
        Usuario usuario = usuarioService.buscarPorId(id);
        usuario.setNome(dados.getNome());
        usuario.setLogin(dados.getLogin());

        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuarioService.verificarSenha(dados.getSenha());
            usuario.setSenha(dados.getSenha());
        }
        return usuarioService.editar(usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable int id) {
        usuarioService.apagar(id);
    }
}
