package com.desafio.ForoHub.Dto;

public record TopicoDatos(
        Long id,
        String titulo,
        String mensaje,
        String curso,
        String creado,
        String nombre
) {
}
