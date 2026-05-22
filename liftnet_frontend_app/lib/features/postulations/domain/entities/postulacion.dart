enum EstadoPostulacion { pendiente, aceptada, rechazada }

class Postulacion {
  final String id;
  final String ofertaId;
  final String postulanteId;
  final EstadoPostulacion estado;
  final DateTime createdAt;
  // Opcional: Podríamos incluir datos de la oferta para mostrar en la lista
  final String? tituloOferta; 
  final String? nombreEmpresa;

  Postulacion({
    required this.id,
    required this.ofertaId,
    required this.postulanteId,
    required this.estado,
    required this.createdAt,
    this.tituloOferta,
    this.nombreEmpresa,
  });
}
