package com.desafio.ForoHub.Repository;

import com.desafio.ForoHub.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  com.desafio.ForoHub.Repository.UsuarioRepository;
@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
}
