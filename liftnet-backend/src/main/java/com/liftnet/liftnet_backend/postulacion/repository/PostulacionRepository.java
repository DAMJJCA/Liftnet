package com.liftnet.liftnet_backend.postulacion.repository;

import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.postulacion.entity.Postulacion;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface PostulacionRepository extends JpaRepository<Postulacion, UUID> {

    boolean existsByOfertaAndPostulante(OfertaTrabajo oferta, PostulanteProfile postulante);
  
    Page<Postulacion> findByPostulante(PostulanteProfile postulante, Pageable pageable);

    Page<Postulacion> findByOferta(OfertaTrabajo oferta, Pageable pageable);

    Optional<Postulacion> findById(UUID id);
}


