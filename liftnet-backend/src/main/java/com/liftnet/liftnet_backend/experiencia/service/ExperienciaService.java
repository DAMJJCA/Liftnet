package com.liftnet.liftnet_backend.experiencia.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.experiencia.dto.ExperienciaRequest;
import com.liftnet.liftnet_backend.experiencia.entity.Experiencia;
import com.liftnet.liftnet_backend.experiencia.repository.ExperienciaRepository;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.repository.PostulanteRepository;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ExperienciaService {

    private final ExperienciaRepository repository;
    private final UserRepository userRepository;
    private final PostulanteRepository postulanteRepository;

    public ExperienciaService(
            ExperienciaRepository repository,
            UserRepository userRepository,
            PostulanteRepository postulanteRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.postulanteRepository = postulanteRepository;
    }

    @Transactional(readOnly = true)
    public Page<Experiencia> listar(String email, Pageable pageable) {

        PostulanteProfile postulante = obtenerPostulante(email);
        return repository.findByPostulante(postulante, pageable);
    }

    public void crear(String email, ExperienciaRequest request) {

        PostulanteProfile postulante = obtenerPostulante(email);

        Experiencia exp = new Experiencia();
        exp.setPostulante(postulante);
        exp.setLugar(request.getLugar());
        exp.setDescripcion(request.getDescripcion());
        exp.setFechaInicio(request.getFechaInicio());
        exp.setFechaFin(request.getFechaFin());

        repository.save(exp);
    }

    public void actualizar(String email, UUID id, ExperienciaRequest request) {

        PostulanteProfile postulante = obtenerPostulante(email);

        Experiencia exp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada"));

        if (!exp.getPostulante().getId().equals(postulante.getId())) {
            throw new IllegalStateException("No puedes editar esta experiencia");
        }

        exp.setLugar(request.getLugar());
        exp.setDescripcion(request.getDescripcion());
        exp.setFechaInicio(request.getFechaInicio());
        exp.setFechaFin(request.getFechaFin());
    }

    public void eliminar(String email, UUID id) {

        PostulanteProfile postulante = obtenerPostulante(email);

        Experiencia exp = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada"));

        if (!exp.getPostulante().getId().equals(postulante.getId())) {
            throw new IllegalStateException("No puedes eliminar esta experiencia");
        }

        repository.delete(exp);
    }

    private PostulanteProfile obtenerPostulante(String email) {

        return postulanteRepository.findByUser(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"))
        ).orElseThrow(() -> new ResourceNotFoundException("Perfil postulante no encontrado"));
    }
}