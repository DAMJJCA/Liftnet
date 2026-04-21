package com.liftnet.liftnet_backend.common.mapper;

import com.liftnet.liftnet_backend.oferta.dto.OfertaResponse;
import com.liftnet.liftnet_backend.oferta.entity.OfertaTrabajo;

public class OfertaMapper {

    private OfertaMapper() {
    }

    public static OfertaResponse toResponse(OfertaTrabajo oferta) {
        return new OfertaResponse(
                oferta.getTitulo(),
                oferta.getDescripcion(),
                oferta.getUbicacion(),
                oferta.isActiva(),
                oferta.getCreatedAt()
        );
    }
}