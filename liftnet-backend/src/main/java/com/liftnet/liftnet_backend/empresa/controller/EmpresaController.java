package com.liftnet.liftnet_backend.empresa.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    @PreAuthorize("hasRole('EMPRESA')")
    @PostMapping("/ofertas")
    public String crearOferta() {
        return "Oferta creada por EMPRESA";
    }
}
