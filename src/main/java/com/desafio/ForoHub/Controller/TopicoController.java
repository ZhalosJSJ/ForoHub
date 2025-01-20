package com.desafio.ForoHub.Controller;

import com.desafio.ForoHub.Dto.TopicoDatos;
import com.desafio.ForoHub.Dto.TopicoRegistrarDatos;
import com.desafio.ForoHub.modelo.Topico;
import com.desafio.ForoHub.modelo.Usuario;
import com.desafio.ForoHub.Repository.TopicoRepository;
import com.desafio.ForoHub.Repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static TopicoDatos apply(Topico topico) {
        return new TopicoDatos(
                (Long) topico.getId(),
                (String) topico.getTitulo(),
                topico.getMessage(),
                (String) topico.getCurso(),
                topico.getCreado().toString(),
                topico.getUsuario().getUsername()
        );
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody @Valid TopicoRegistrarDatos topicoRegistrarDatos, Authentication authentication) {
        String nombre = authentication.getName();

        UserDetails usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        Topico topico = new Topico(topicoRegistrarDatos, (Usuario) usuario);
        topicoRepository.save(topico);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(topico.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id, Authentication authentication) {
        String nombre = authentication.getName();

        UserDetails usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null) {
            return ResponseEntity.status(404).body("No encontrado");
        }

        if (!topico.getUsuario().getUsername().equals(nombre)) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar(Authentication authentication) {
        String nombre = authentication.getName();

        UserDetails usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        List<TopicoDatos> topicos = topicoRepository.findAll().stream()
                .map(TopicoController::apply)
                .toList();

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarUno(@PathVariable("id") Long id, Authentication authentication) {
        String nombre = authentication.getName();

        UserDetails usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null) {
            return ResponseEntity.status(404).body("No encontrado");
        }

        TopicoDatos topicoDatos = new TopicoDatos((Long) topico.getId(), (String) topico.getTitulo(), topico.getMessage(), (String) topico.getCurso(), topico.getCreado().toString(), topico.getUsuario().getUsername());

        return ResponseEntity.ok(topicoDatos);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> actualizarTopico(@PathVariable("id") Long id,
                                              @RequestBody @Valid TopicoRegistrarDatos topicoRegisterData,
                                              Authentication authentication) {
        String nombre = authentication.getName();

        UserDetails usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        Topico topico = topicoRepository.findById(id).orElse(null);
        if (topico == null) {
            return ResponseEntity.status(404).body("No encontrado");
        }

        if (!topico.getUsuario().getUsername().equals(nombre)) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        topico.setTitulo(topicoRegisterData.titulo());
        topico.setMensaje(topicoRegisterData.mensaje());
        topico.setCurso(topicoRegisterData.curso());
        topicoRepository.save(topico);
        return ResponseEntity.ok("Modificado");
    }
}

