package com.liftnet.liftnet_backend.oferta.service;

import com.liftnet.liftnet_backend.common.exception.ResourceNotFoundException;
import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.empresa.repository.EmpresaRepository;
import com.liftnet.liftnet_backend.oferta.dto.OfertaRequest;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;
import com.liftnet.liftnet_backend.oferta.repository.OfertaRepository;
import com.liftnet.liftnet_backend.user.entity.User;
import com.liftnet.liftnet_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfertaService {

    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final UserRepository userRepository;

    public OfertaService(OfertaRepository ofertaRepository,
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

        return ofertaRepository.save(oferta);
    }

    // EMPRESA VE SUS OFERTAS
    public List<OfertaTrabajo> getMisOfertas(String email) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        return ofertaRepository.findByEmpresa(empresa);
    }

    // POSTULANTE VE OFERTAS ACTIVAS
    public List<OfertaTrabajo> getOfertasActivas(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) {
            return ofertaRepository.findByActivaTrue();
        }
        return ofertaRepository.findByActivaTrueAndUbicacionIgnoreCase(ubicacion);
    }

    // EMPRESA CIERRA UNA OFERTA
    public void cerrarOferta(String email, UUID ofertaId) {
        User user = findUser(email);
        EmpresaProfile empresa = empresaRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Empresa profile not found"));

        OfertaTrabajo oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Oferta not found"));

        if (!oferta.getEmpresa().getId().equals(empresa.getId())) {
            throw new SecurityException("Cannot modify foreign offer");
        }

        oferta.setActiva(false);
        ofertaRepository.save(oferta);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}