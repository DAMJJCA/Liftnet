package com.liftnet.liftnet_backend.postulante.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.postulante.dto.PostulanteProfileRequest;
import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.postulante.repository.PostulanteRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostulanteService {

    private final PostulanteRepository postulanteRepository;
    private final UserRepository userRepository;

    public PostulanteService(PostulanteRepository postulanteRepository,
                             UserRepository userRepository) {
        this.postulanteRepository = postulanteRepository;
        this.userRepository = userRepository;
    }

    // OBTENER MI PERFIL
    public PostulanteProfile getMyProfile(String email) {
        User user = findUserByEmail(email);

        return postulanteRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulante profile not found"));
    }

    // CREAR PERFIL 
    public PostulanteProfile createProfile(String email, PostulanteProfileRequest request) {
        User user = findUserByEmail(email);

        if (postulanteRepository.findByUser(user).isPresent()) {
            throw new IllegalStateException("Postulante profile already exists");
        }

        PostulanteProfile profile = new PostulanteProfile();
        profile.setUser(user);
        profile.setNombre(request.getNombre());
        profile.setApellidos(request.getApellidos());
        profile.setUbicacion(request.getUbicacion());
        profile.setTelefono(request.getTelefono());
        profile.setBio(request.getBio());
        profile.setDisponible(true);

        return postulanteRepository.save(profile);
    }

    // ACTUALIZAR PERFIL
    public PostulanteProfile updateProfile(String email, PostulanteProfileRequest request) {
        User user = findUserByEmail(email);

        PostulanteProfile profile = postulanteRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulante profile not found"));

        profile.setNombre(request.getNombre());
        profile.setApellidos(request.getApellidos());
        profile.setUbicacion(request.getUbicacion());
        profile.setTelefono(request.getTelefono());
        profile.setBio(request.getBio());

        return postulanteRepository.save(profile);
    }

    // MARCAR DISPONIBILIDAD
    public void updateDisponibilidad(String email, boolean disponible) {
        User user = findUserByEmail(email);

        PostulanteProfile profile = postulanteRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulante profile not found"));

        profile.setDisponible(disponible);
        postulanteRepository.save(profile);
    }

    // MÉTODO PRIVADO DE APOYO
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}