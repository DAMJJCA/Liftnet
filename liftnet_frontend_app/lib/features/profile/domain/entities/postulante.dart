class Postulante {
  final String id;
  final String userId;
  final String nombre;
  final String? apellidos;
  final String? ubicacion;
  final String? bio;
  final String? telefono;
  final bool disponible;
  final String? fotoUrl;
  final String? cvUrl;

  Postulante({
    required this.id,
    required this.userId,
    required this.nombre,
    this.apellidos,
    this.ubicacion,
    this.bio,
    this.telefono,
    required this.disponible,
    this.fotoUrl,
    this.cvUrl,
  });
}
