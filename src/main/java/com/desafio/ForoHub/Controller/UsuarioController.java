package com.desafio.ForoHub.Controller;

import com.desafio.ForoHub.Dto.UsuarioDatos;
import com.desafio.ForoHub.Dto.UsuarioRegistrarDatos;
import com.desafio.ForoHub.ManejoDeErrores.Seguridad.JwtData;
import com.desafio.ForoHub.ManejoDeErrores.Seguridad.TokenService;
import com.desafio.ForoHub.modelo.Usuario;
import com.desafio.ForoHub.Repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@RequestBody @Valid UsuarioRegistrarDatos usuarioRegistrarDatos) {
        if (usuarioRepository.findByNombre(usuarioRegistrarDatos.getNombre()) != null ||
                usuarioRepository.findByEmail(usuarioRegistrarDatos.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists");
        }

        String encryptedPassword = passwordEncoder.encode(usuarioRegistrarDatos.getClave());
        Usuario usuario = new Usuario(usuarioRegistrarDatos.getNombre(), usuarioRegistrarDatos.getEmail(), encryptedPassword);
        usuarioRepository.save(usuario);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{usuarioId}")
                .buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid UsuarioDatos usuarioDatos) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                usuarioDatos.nombre(),
                usuarioDatos.clave()
        );
        try {
            var usuarioAutenticado = authenticationManager.authenticate(authToken);
            var JWTtoken = tokenService.generateToken((Usuario) usuarioAutenticado.getPrincipal());
            return ResponseEntity.ok(new JwtData(JWTtoken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}
