class LoginResponse {
  final String id;
  final String token;
  final String email;
  final String nombre;
  final String rol;

  LoginResponse({
    required this.id,
    required this.token,
    required this.email,
    required this.nombre,
    required this.rol,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      id: json['id'],
      token: json['token'],
      email: json['email'],
      nombre: json['nombre'],
      rol: json['rol'],
    );
  }
}
