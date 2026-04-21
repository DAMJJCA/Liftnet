package com.liftnet.liftnet_backend.oferta.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.oferta.dto.OfertaRequest;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.oferta.repository.OfertaRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OfertaService {

    private static final Logger log =
            LoggerFactory.getLogger(OfertaService.class);

    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final UserRepository userRepository;

    public OfertaService(
            OfertaRepository ofertaRepository,
            EmpresaRepository empresaRepository,
            UserRepository userRepository) {

        this.ofertaRepository = ofertaRepository;
        this.empresaRepository = empresaRepository;
        this.userRepository = userRepository;
    }

    // EMPRESA CREA OFERTA
    public OfertaTrabajo crearOferta(String email, OfertaRequest request) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        OfertaTrabajo oferta = new OfertaTrabajo();
        oferta.setEmpresa(empresa);
        oferta.setTitulo(request.getTitulo());
        oferta.setDescripcion(request.getDescripcion());
        oferta.setUbicacion(request.getUbicacion());

        OfertaTrabajo saved = ofertaRepository.save(oferta);

        log.info("Empresa {} creó oferta {}",
                empresa.getNombreEmpresa(),
                saved.getId());

        return saved;
    }

    // EMPRESA VE SUS OFERTAS
    public Page<OfertaTrabajo> getMisOfertas(String email, Pageable pageable) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        log.info("Empresa {} consulta sus ofertas", empresa.getNombreEmpresa());

        return ofertaRepository.findByEmpresa(empresa, pageable);
    }

    // POSTULANTE VE OFERTAS
    public Page<OfertaTrabajo> getOfertasActivas(
            String ubicacion,
            Pageable pageable) {

        log.info("Consulta de ofertas activas. Ubicación: {}",
                ubicacion != null ? ubicacion : "todas");

        if (ubicacion == null || ubicacion.isBlank()) {
            return ofertaRepository.findByActivaTrue(pageable);
        }

        return ofertaRepository
                .findByActivaTrueAndUbicacionIgnoreCase(ubicacion, pageable);
    }

    // EMPRESA CIERRA OFERTA
    public void cerrarOferta(String email, UUID ofertaId) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oferta not found"));

        if (!oferta.getEmpresa().getId().equals(empresa.getId())) {
            log.warn("Empresa {} intentó cerrar oferta ajena {}",
                    empresa.getNombreEmpresa(), ofertaId);

            throw new SecurityException("Not your offer");
        }

        oferta.setActiva(false);
        ofertaRepository.save(oferta);

        log.info("Oferta {} cerrada por empresa {}",
                ofertaId, empresa.getNombreEmpresa());
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}