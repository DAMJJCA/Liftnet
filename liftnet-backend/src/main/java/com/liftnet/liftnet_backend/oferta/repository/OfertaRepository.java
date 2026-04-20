package com.liftnet.liftnet_backend.oferta.repository;

import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfertaRepository extends JpaRepository<OfertaTrabajo, UUID> {

    List<OfertaTrabajo> findByEmpresa(EmpresaProfile empresa);

    List<OfertaTrabajo> findByActivaTrue();

    List<OfertaTrabajo> findByActivaTrueAndUbicacionIgnoreCase(String ubicacion);
}
