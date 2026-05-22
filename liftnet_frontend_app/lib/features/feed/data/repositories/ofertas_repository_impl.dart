import 'package:dio/dio.dart';
import '../../domain/entities/oferta.dart';
import '../../domain/repositories/ofertas_repository.dart';
import '../models/oferta_model.dart';
import '../../../../core/constants/api_constants.dart';

class OfertasRepositoryImpl implements OfertasRepository {
  final Dio _dio;

  OfertasRepositoryImpl(this._dio);

  @override
  Future<List<Oferta>> getOfertasActivas({String? ubicacion}) async {
    try {
      final response = await _dio.get(
        ApiConstants.ofertas,
        queryParameters: ubicacion != null ? {'ubicacion': ubicacion} : null,
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data
            .map((json) => OfertaModel.fromJson(json).toEntity())
            .toList();
      } else {
        throw Exception('Error al cargar ofertas');
      }
    } on DioException catch (e) {
      throw Exception('Error de red: ${e.message}');
    }
  }
}
