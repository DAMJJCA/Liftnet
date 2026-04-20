package com.liftnet.liftnet_backend.empresa.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
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

    public EmpresaService(EmpresaRepository empresaRepository,
                          UserRepository userRepository) {
        this.empresaRepository = empresaRepository;
        this.userRepository = userRepository;
    }

    public EmpresaProfile getMyProfile(String email) {
        User user = findUser(email);
        return empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));
    }

    public EmpresaProfile createProfile(String email, EmpresaProfileRequest request) {
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

        return empresaRepository.save(profile);
    }

    public EmpresaProfile updateProfile(String email, EmpresaProfileRequest request) {
        User user = findUser(email);

        EmpresaProfile profile = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        profile.setNombreEmpresa(request.getNombreEmpresa());
        profile.setUbicacion(request.getUbicacion());
        profile.setTelefono(request.getTelefono());
        profile.setDescripcion(request.getDescripcion());

        return empresaRepository.save(profile);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}