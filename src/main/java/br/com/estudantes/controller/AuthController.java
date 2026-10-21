package br.com.estudantes.controller;

import br.com.estudantes.dto.LoginRequest;
import br.com.estudantes.model.Usuario;
import br.com.estudantes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public Usuario login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.login(request.login(), request.senha());
    }
}
