package com.liftnet.liftnet_backend.postulacion.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.oferta.repository.OfertaRepository;
import com.liftnet.liftnet_backend.postulacion.entity.EstadoPostulacion;
import com.liftnet.liftnet_backend.postulacion.entity.Postulacion;
import com.liftnet.liftnet_backend.postulacion.repository.PostulacionRepository;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.repository.PostulanteRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostulacionService {

    private static final Logger log =
            LoggerFactory.getLogger(PostulacionService.class);

    private final PostulacionRepository postulacionRepository;
    private final UserRepository userRepository;
    private final PostulanteRepository postulanteRepository;
    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;

    public PostulacionService(
            PostulacionRepository postulacionRepository,
            UserRepository userRepository,
            PostulanteRepository postulanteRepository,
            OfertaRepository ofertaRepository,
            EmpresaRepository empresaRepository) {

        this.postulacionRepository = postulacionRepository;
        this.userRepository = userRepository;
        this.postulanteRepository = postulanteRepository;
        this.ofertaRepository = ofertaRepository;
        this.empresaRepository = empresaRepository;
    }

    // POSTULANTE SE POSTULA
    public void postular(String email, UUID ofertaId) {
        User user = findUser(email);
        PostulanteProfile postulante = postulanteRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulante profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oferta not found"));

        if (!oferta.isActiva()) {
            log.warn("Postulante {} intentó postular a oferta cerrada {}",
                    email, ofertaId);
            throw new IllegalStateException("Cannot apply to inactive offer");
        }

        if (postulacionRepository.existsByOfertaAndPostulante(oferta, postulante)) {
            log.warn("Postulante {} intentó postular dos veces a oferta {}",
                    email, ofertaId);
            throw new IllegalStateException("Already applied to this offer");
        }

        Postulacion postulacion = new Postulacion();
        postulacion.setOferta(oferta);
        postulacion.setPostulante(postulante);

        postulacionRepository.save(postulacion);

        log.info("Postulante {} se postuló a oferta {}",
                email, ofertaId);
    }

    // POSTULANTE VE SUS POSTULACIONES
    public Page<Postulacion> getMisPostulaciones(
            String email,
            Pageable pageable) {

        User user = findUser(email);
        PostulanteProfile postulante = postulanteRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulante profile not found"));

        log.info("Postulante {} consulta sus postulaciones", email);

        return postulacionRepository.findByPostulante(postulante, pageable);
    }

    // EMPRESA VE POSTULACIONES
    public Page<Postulacion> getPostulacionesOferta(
            String email,
            UUID ofertaId,
            Pageable pageable) {

        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oferta not found"));

        if (!oferta.getEmpresa().getId().equals(empresa.getId())) {
            log.warn("Empresa {} intentó ver postulaciones de oferta ajena {}",
                    empresa.getNombreEmpresa(), ofertaId);

            throw new SecurityException("Not your offer");
        }

        log.info("Empresa {} consulta postulaciones de oferta {}",
                empresa.getNombreEmpresa(), ofertaId);

        return postulacionRepository.findByOferta(oferta, pageable);
    }

    // EMPRESA CAMBIA ESTADO
    public void actualizarEstado(
            String email,
            UUID postulacionId,
            EstadoPostulacion estado) {

        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulacion not found"));

        if (!postulacion.getOferta().getEmpresa().getId().equals(empresa.getId())) {
            log.warn("Empresa {} intentó modificar postulación ajena {}",
                    empresa.getNombreEmpresa(), postulacionId);

            throw new SecurityException("Not your offer");
        }

        postulacion.setEstado(estado);
        postulacionRepository.save(postulacion);

        log.info("Empresa {} cambió estado de postulación {} a {}",
                empresa.getNombreEmpresa(),
                postulacionId,
                estado);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}