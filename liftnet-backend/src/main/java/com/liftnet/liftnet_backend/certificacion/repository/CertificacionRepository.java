package com.liftnet.liftnet_backend.certificacion.repository;

import com.liftnet.liftnet_backend.certificacion.entity.Certificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificacionRepository extends JpaRepository<Certificacion, UUID> {
    // JpaRepository ya nos da:
    // - findAll()
    // - findById()
    // - save()
    // - delete()
}
