package com.liftnet.liftnet_backend.empresa.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.liftnet.liftnet_backend.empresa.dto.EmpresaProfileRequest;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UserRepository userRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UserRepository userRepository) {
        this.empresaRepository = empresaRepository;
        this.userRepository = userRepository;
    }

    // MÉTODO CLAVE: Obtiene el email del token actual
    private String getAuthenticatedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // OBTENER MI PERFIL (Ya no pide String email)
    public EmpresaProfile getMyProfile() {
        String email = getAuthenticatedUserEmail(); // <--- Seguridad Real
        User user = findUser(email);

        return empresaRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa profile not found"));
    }

    // CREAR PERFIL (Ya no pide String email)
    public EmpresaProfile createProfile(EmpresaProfileRequest request) {
        String email = getAuthenticatedUserEmail();
        User user = findUser(email);

        if (empresaRepository.findByUser(user).isPresent()) {
            throw new IllegalStateException("Empresa profile already exists");
        }

        EmpresaProfile profile = new EmpresaProfile();
        profile.setUser(user);
        profile.setNombreEmpresa(request.getNombreEmpresa());
        profile.setUbicacion(request.getUbicacion());
        profile.setTelefono(request.getTelefono());
        profile.setDescripcion(request.getDescripcion());
        profile.setFotoUrl(request.getFotoUrl());

        return empresaRepository.save(profile);
    }

    // ACTUALIZAR PERFIL (Ya no pide String email)
    public EmpresaProfile updateProfile(EmpresaProfileRequest request) {
        String email = getAuthenticatedUserEmail();
        User user = findUser(email);

        EmpresaProfile profile = empresaRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa profile not found"));

        profile.setNombreEmpresa(request.getNombreEmpresa());
        profile.setUbicacion(request.getUbicacion());
        profile.setTelefono(request.getTelefono());
        profile.setDescripcion(request.getDescripcion());
        profile.setFotoUrl(request.getFotoUrl());

        return empresaRepository.save(profile);
    }

    public void createEmptyProfile(User user) {
        if (empresaRepository.findByUser(user).isPresent()) return;
        EmpresaProfile profile = new EmpresaProfile();
        profile.setUser(user);
        empresaRepository.save(profile);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}