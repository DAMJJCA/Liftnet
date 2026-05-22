import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../../../../core/constants/api_constants.dart';
import '../models/login_response.dart';

class AuthRepository {
  final Dio _dio;
  final _storage = const FlutterSecureStorage();

  AuthRepository(this._dio);

  Future<LoginResponse> login(String email, String password) async {
    try {
      final response = await _dio.post(
        ApiConstants.login,
        data: {'email': email, 'password': password},
      );
      
      final loginData = LoginResponse.fromJson(response.data);
      
      await _storage.write(key: 'jwt_token', value: loginData.token);
      await _storage.write(key: 'user_role', value: loginData.rol);
      await _storage.write(key: 'user_id', value: loginData.id);
      
      return loginData;
    } catch (e) {
      throw Exception('Error de autenticación');
    }
  }

  Future<LoginResponse> register({
    required String email,
    required String password,
    required String nombre,
    required String role, // 'POSTULANTE' o 'EMPRESA'
  }) async {
    try {
      final response = await _dio.post(
        ApiConstants.register,
        data: {
          'email': email,
          'password': password,
          'nombre': nombre,
          'role': role,
        },
      );

      final loginData = LoginResponse.fromJson(response.data);

      await _storage.write(key: 'jwt_token', value: loginData.token);
      await _storage.write(key: 'user_role', value: loginData.rol);
      await _storage.write(key: 'user_id', value: loginData.id);

      return loginData;
    } catch (e) {
      throw Exception('Error en el registro');
    }
  }

  Future<void> logout() async {
    await _storage.delete(key: 'jwt_token');
    await _storage.delete(key: 'user_role');
  }

  Future<String?> getToken() async {
    return await _storage.read(key: 'jwt_token');
  }
}
