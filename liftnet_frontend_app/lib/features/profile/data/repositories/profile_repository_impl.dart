import 'package:dio/dio.dart';
import '../../domain/entities/postulante.dart';
import '../../../../core/constants/api_constants.dart';

class ProfileRepository {
  final Dio _dio;

  ProfileRepository(this._dio);

  Future<Postulante> getMiPerfil() async {
    try {
      final response = await _dio.get('${ApiConstants.baseUrl}/postulantes/mi-perfil');
      // Aquí mapearías el JSON a la entidad Postulante
      // return PostulanteModel.fromJson(response.data).toEntity();
      throw UnimplementedError('Mapeo de perfil no implementado');
    } catch (e) {
      throw Exception('Error al obtener perfil');
    }
  }

  Future<void> actualizarCvUrl(String cvUrl) async {
    try {
      await _dio.put(
        '${ApiConstants.baseUrl}/postulantes/actualizar-cv',
        data: {'cvUrl': cvUrl},
      );
    } catch (e) {
      throw Exception('Error al actualizar URL del CV en el backend');
    }
  }

  Future<void> actualizarFotoUrl(String fotoUrl) async {
    try {
      await _dio.put(
        '${ApiConstants.baseUrl}/postulantes/actualizar-foto',
        data: {'fotoUrl': fotoUrl},
      );
    } catch (e) {
      throw Exception('Error al actualizar URL de la foto en el backend');
    }
  }
}
