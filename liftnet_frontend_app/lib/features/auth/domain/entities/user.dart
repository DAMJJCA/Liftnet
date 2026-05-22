enum UserRole { postulante, empresa }

class User {
  final String id; // UUID
  final String email;
  final String nombre;
  final UserRole rol;

  User({
    required this.id,
    required this.email,
    required this.nombre,
    required this.rol,
  });
}
