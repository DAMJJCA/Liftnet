package com.liftnet.liftnet_backend.oferta.repository;

import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfertaRepository extends JpaRepository<OfertaTrabajo, UUID> {

    // EMPRESA VE SUS OFERTAS (PAGINADO)
    Page<OfertaTrabajo> findByEmpresa(
            EmpresaProfile empresa,
            Pageable pageable
    );

    // POSTULANTE VE OFERTAS ACTIVAS (PAGINADO)
    Page<OfertaTrabajo> findByActivaTrue(Pageable pageable);

    // FILTRO POR UBICACIÓN (PAGINADO)
    Page<OfertaTrabajo> findByActivaTrueAndUbicacionIgnoreCase(
            String ubicacion,
            Pageable pageable
    );
}
