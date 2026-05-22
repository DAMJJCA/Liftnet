import 'package:dio/dio.dart';
import '../../../feed/domain/entities/oferta.dart';
import '../../../../core/constants/api_constants.dart';
import '../../../feed/data/models/oferta_model.dart';

class CompanyRepository {
  final Dio _dio;

  CompanyRepository(this._dio);

  Future<List<Oferta>> getMisOfertas() async {
    try {
      final response = await _dio.get(ApiConstants.misOfertas);
      final List<dynamic> data = response.data;
      return data.map((json) => OfertaModel.fromJson(json).toEntity()).toList();
    } catch (e) {
      throw Exception('Error al obtener mis ofertas');
    }
  }

  Future<void> crearOferta(Map<String, dynamic> ofertaData) async {
    try {
      await _dio.post(ApiConstants.ofertas, data: ofertaData);
    } catch (e) {
      throw Exception('Error al crear la oferta');
    }
  }

  Future<void> cerrarOferta(String id) async {
    try {
      await _dio.put('${ApiConstants.ofertas}/$id/cerrar');
    } catch (e) {
      throw Exception('Error al cerrar la oferta');
    }
  }
}
