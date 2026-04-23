package com.liftnet.liftnet_backend.certificacion.service;

import com.liftnet.liftnet_backend.certificacion.dto.AsignarCertificacionRequest;
import com.liftnet.liftnet_backend.certificacion.entity.Certificacion;
import com.liftnet.liftnet_backend.certificacion.entity.PostulanteCertificacion;
import com.liftnet.liftnet_backend.certificacion.repository.CertificacionRepository;
import com.liftnet.liftnet_backend.certificacion.repository.PostulanteCertificacionRepository;
import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.repository.PostulanteRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CertificacionService {

    private final CertificacionRepository certificacionRepository;
    private final PostulanteCertificacionRepository postulanteCertificacionRepository;
    private final UserRepository userRepository;
    private final PostulanteRepository postulanteRepository;

    public CertificacionService(
            CertificacionRepository certificacionRepository,
            PostulanteCertificacionRepository postulanteCertificacionRepository,
            UserRepository userRepository,
            PostulanteRepository postulanteRepository) {

        this.certificacionRepository = certificacionRepository;
        this.postulanteCertificacionRepository = postulanteCertificacionRepository;
        this.userRepository = userRepository;
        this.postulanteRepository = postulanteRepository;
    }

    @Transactional(readOnly = true)
    public Page<PostulanteCertificacion> listarCertificacionesDePostulante(
            String email,
            Pageable pageable) {

        PostulanteProfile postulante = obtenerPostulante(email);
        return postulanteCertificacionRepository.findByPostulante(postulante, pageable);
    }

    public void asignarCertificacion(String email, AsignarCertificacionRequest request) {

        PostulanteProfile postulante = obtenerPostulante(email);

        Certificacion certificacion = certificacionRepository
                .findById(request.getCertificacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Certificación no encontrada"));

        postulanteCertificacionRepository
                .findByPostulanteAndCertificacion(postulante, certificacion)
                .ifPresent(pc -> {
                    throw new IllegalStateException("El postulante ya posee esta certificación");
                });

        PostulanteCertificacion pc = new PostulanteCertificacion();
        pc.setPostulante(postulante);
        pc.setCertificacion(certificacion);
        pc.setFechaObtencion(request.getFechaObtencion());
        pc.setFechaExpiracion(request.getFechaExpiracion());

        postulanteCertificacionRepository.save(pc);
    }

    public void eliminarCertificacion(String email, UUID id) {

        PostulanteProfile postulante = obtenerPostulante(email);

        PostulanteCertificacion pc = postulanteCertificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificación no encontrada"));

        if (!pc.getPostulante().getId().equals(postulante.getId())) {
            throw new IllegalStateException("No tienes permiso para eliminar esta certificación");
        }

        postulanteCertificacionRepository.delete(pc);
    }

    private PostulanteProfile obtenerPostulante(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return postulanteRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil postulante no encontrado"));
    }
}