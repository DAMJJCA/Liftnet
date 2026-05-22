import 'package:freezed_annotation/freezed_annotation.dart';
import '../../domain/entities/postulacion.dart';

part 'postulacion_model.freezed.dart';
part 'postulacion_model.g.dart';

@freezed
class PostulacionModel with _$PostulacionModel {
  const factory PostulacionModel({
    required String id,
    required String ofertaId,
    required String postulanteId,
    required String estado, // PENDIENTE, ACEPTADA, RECHAZADA
    required DateTime createdAt,
    String? tituloOferta,
    String? nombreEmpresa,
  }) = _PostulacionModel;

  factory PostulacionModel.fromJson(Map<String, dynamic> json) => _$PostulacionModelFromJson(json);

  Postulacion toEntity() {
    EstadoPostulacion estadoEnum;
    switch (estado.toUpperCase()) {
      case 'ACEPTADA':
        estadoEnum = EstadoPostulacion.aceptada;
        break;
      case 'RECHAZADA':
        estadoEnum = EstadoPostulacion.rechazada;
        break;
      default:
        estadoEnum = EstadoPostulacion.pendiente;
    }

    return Postulacion(
      id: id,
      ofertaId: ofertaId,
      postulanteId: postulanteId,
      estado: estadoEnum,
      createdAt: createdAt,
      tituloOferta: tituloOferta,
      nombreEmpresa: nombreEmpresa,
    );
  }
}
