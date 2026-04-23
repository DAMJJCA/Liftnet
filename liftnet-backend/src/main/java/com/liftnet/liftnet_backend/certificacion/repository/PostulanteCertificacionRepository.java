package com.liftnet.liftnet_backend.certificacion.repository;

import com.liftnet.liftnet_backend.certificacion.entity.PostulanteCertificacion;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.certificacion.entity.Certificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostulanteCertificacionRepository
        extends JpaRepository<PostulanteCertificacion, UUID> {


    // Todas las certificaciones de un postulante
    Page<PostulanteCertificacion> findByPostulante(
            PostulanteProfile postulante,
            Pageable pageable
    );

    // Saber si un postulante ya tiene una certificación concreta
    Optional<PostulanteCertificacion> findByPostulanteAndCertificacion(
            PostulanteProfile postulante,
            Certificacion certificacion
    );
}