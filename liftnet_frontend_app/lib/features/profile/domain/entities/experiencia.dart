class Experiencia {
  final String id;
  final String postulanteId;
  final String lugar;
  final String descripcion;
  final DateTime? fechaInicio;
  final DateTime? fechaFin;
  final String? archivoUrl;

  Experiencia({
    required this.id,
    required this.postulanteId,
    required this.lugar,
    required this.descripcion,
    this.fechaInicio,
    this.fechaFin,
    this.archivoUrl,
  });
}
