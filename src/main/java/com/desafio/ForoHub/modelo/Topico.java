package com.desafio.ForoHub.modelo;

import com.desafio.ForoHub.Dto.TopicoRegistrarDatos;
import jakarta.persistence.*;
import lombok. *;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@Entity(name = "Topico")
@Table(name = "topicos")
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;

    private boolean estado;

    private String curso;

    private LocalDate creado;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Topico(TopicoRegistrarDatos topicoRegistrarDatos, Usuario usuario) {
        this.titulo = topicoRegistrarDatos.titulo();
        this.mensaje = topicoRegistrarDatos.mensaje();
        this.curso = topicoRegistrarDatos.curso();
        this.creado = LocalDate.now();
        this.usuario = usuario;
        this.estado = true;
    }

    public Topico() {
    }

    public String getMessage() {
        return mensaje;
    }

    public Object getId() {
        return null;
    }

    public UserDetails getUsuario() {
        return null;
    }

    public Object getTitulo() {
        return null;
    }

    public Object getCurso() {
        return null;
    }

    public Boolean getCreado() {
        return null;
    }

    public void setTitulo(String titulo) {
    }

    public void setMensaje(String mensaje) {
    }

    public void setCurso(String curso) {
    }
}


