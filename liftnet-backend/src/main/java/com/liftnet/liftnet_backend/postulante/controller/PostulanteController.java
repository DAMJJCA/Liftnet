package com.liftnet.liftnet_backend.postulante.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postulante")
public class PostulanteController {

    @PreAuthorize("hasRole('POSTULANTE')")
    @GetMapping("/ofertas")
    public String verOfertas() {
        return "Viendo ofertas como POSTULANTE";
    }
}