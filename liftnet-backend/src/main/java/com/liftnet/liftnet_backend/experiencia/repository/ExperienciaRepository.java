package com.liftnet.liftnet_backend.experiencia.repository;

import com.liftnet.liftnet_backend.experiencia.entity.Experiencia;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExperienciaRepository extends JpaRepository<Experiencia, UUID> {

    Page<Experiencia> findByPostulante(
            PostulanteProfile postulante,
            Pageable pageable
    );
}