CREATE TABLE topicos (
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    estado BOOLEAN NOT NULL,
    curso VARCHAR(100) NOT NULL,
    usuario_id BIGINT NOT NULL,
    creado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_topico_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (usuario_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

