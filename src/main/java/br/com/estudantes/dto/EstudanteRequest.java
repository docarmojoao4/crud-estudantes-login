package br.com.estudantes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record EstudanteRequest(
        @NotBlank(message = "O nome do estudante é obrigatório") String nome,
        @NotBlank(message = "O CPF do estudante é obrigatório") String cpf,
        @NotNull(message = "A data de nascimento é obrigatória")
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        @NotNull(message = "O usuarioId é obrigatório") Integer usuarioId) {
}
