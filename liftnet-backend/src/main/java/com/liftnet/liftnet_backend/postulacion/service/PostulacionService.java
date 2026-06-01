package com.liftnet.liftnet_backend.postulacion.service;

import com.liftnet.liftnet_backend.certificacion.dto.CertificacionPostulanteResponse;
import com.liftnet.liftnet_backend.certificacion.entity.PostulanteCertificacion;
import com.liftnet.liftnet_backend.certificacion.repository.PostulanteCertificacionRepository;
import com.liftnet.liftnet_backend.common.exception.DuplicatePostulacionException;
import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.experiencia.entity.Experiencia;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.oferta.repository.OfertaRepository;
import com.liftnet.liftnet_backend.postulacion.dto.PostulantePostulacionResponse;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostulacionService {

    private static final Logger log = LoggerFactory.getLogger(PostulacionService.class);

    private final PostulacionRepository postulacionRepository;
    private final UserRepository userRepository;
    private final PostulanteRepository postulanteRepository;
    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final PostulanteCertificacionRepository certificacionRepository;

    public PostulacionService(
            PostulacionRepository postulacionRepository,
            UserRepository userRepository,
            PostulanteRepository postulanteRepository,
            OfertaRepository ofertaRepository,
            EmpresaRepository empresaRepository,
            PostulanteCertificacionRepository certificacionRepository) {

        this.postulacionRepository = postulacionRepository;
        this.userRepository = userRepository;
        this.postulanteRepository = postulanteRepository;
        this.ofertaRepository = ofertaRepository;
        this.empresaRepository = empresaRepository;
        this.certificacionRepository = certificacionRepository;
    }

    // POSTULANTE SE POSTULA
    public void postular(String email, UUID ofertaId) {
        User user = findUser(email);
        PostulanteProfile postulante = postulanteRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Postulante profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta not found"));

        if (!oferta.isActiva()) {
            throw new IllegalStateException("Cannot apply to inactive offer");
        }
        if (postulacionRepository.existsByOfertaAndPostulante(oferta, postulante)) {
            throw new DuplicatePostulacionException("Already applied to this offer");
        }

        Postulacion postulacion = new Postulacion();
        postulacion.setOferta(oferta);
        postulacion.setPostulante(postulante);
        postulacionRepository.save(postulacion);
        log.info("Postulante {} se postuló a oferta {}", email, ofertaId);
    }

    // POSTULANTE VE SUS POSTULACIONES
    public Page<Postulacion> getMisPostulaciones(String email, Pageable pageable) {
        User user = findUser(email);
        PostulanteProfile postulante = postulanteRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Postulante profile not found"));

        return postulacionRepository.findByPostulante(postulante, pageable);
    }

    // EMPRESA VE POSTULACIONES
    public Page<PostulantePostulacionResponse> getPostulacionesOferta(
            String email, UUID ofertaId, Pageable pageable) {

        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta not found"));

        if (!oferta.getEmpresa().getId().equals(empresa.getId())) {
            throw new SecurityException("Not your offer");
        }

        Page<Postulacion> postulaciones = postulacionRepository.findByOferta(oferta, pageable);

        return postulaciones.map(p -> {
            PostulanteProfile postulante = p.getPostulante();
            List<Experiencia> experiencias = postulante.getExperiencias();

            List<PostulanteCertificacion> certs = certificacionRepository
                    .findByPostulante(postulante, Pageable.unpaged()).getContent();

            List<CertificacionPostulanteResponse> certsDto = certs.stream().map(c ->
                    new CertificacionPostulanteResponse(
                            c.getId(), c.getCertificacion().getId(), c.getCertificacion().getNombre(),
                            c.getCertificacion().getEntidad(), c.getFechaObtencion(),
                            c.getFechaExpiracion(), c.getArchivoUrl()
                    )
            ).collect(Collectors.toList());

            return new PostulantePostulacionResponse(
                    p.getId(), postulante.getNombre(), postulante.getApellidos(),
                    p.getEstado(), p.getCreatedAt(), experiencias, certsDto, postulante.getCvUrl()
            );
        });
    }

    // POSTULANTE RETIRA SU CANDIDATURA (solo si está PENDIENTE)
    public void retirarPostulacion(String email, UUID postulacionId) {
        User user = findUser(email);
        PostulanteProfile postulante = postulanteRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Postulante profile not found"));

        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion not found"));

        if (!postulacion.getPostulante().getId().equals(postulante.getId())) {
            throw new SecurityException("Not your application");
        }

        if (postulacion.getEstado() != EstadoPostulacion.PENDIENTE) {
            throw new IllegalStateException("Solo puedes retirar candidaturas en estado PENDIENTE");
        }

        postulacionRepository.deleteById(postulacionId);
        log.info("Postulante {} retiró postulación {}", email, postulacionId);
    }

    // EMPRESA CAMBIA ESTADO
    public void actualizarEstado(String email, UUID postulacionId, EstadoPostulacion estado) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa profile not found"));

        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion not found"));

        if (!postulacion.getOferta().getEmpresa().getId().equals(empresa.getId())) {
            throw new SecurityException("Not your offer");
        }

        postulacion.setEstado(estado);
        postulacionRepository.save(postulacion);
        log.info("Empresa {} cambió estado de postulación {} a {}", empresa.getNombreEmpresa(), postulacionId, estado);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
