import 'package:freezed_annotation/freezed_annotation.dart';
import '../../domain/entities/oferta.dart';

part 'oferta_model.freezed.dart';
part 'oferta_model.g.dart';

@freezed
class OfertaModel with _$OfertaModel {
  const factory OfertaModel({
    required String id,
    required String empresaId,
    required String titulo,
    required String descripcion,
    required String ubicacion,
    required String estudiosMinimos,
    required String experienciaMinima,
    required String idiomas,
    required String nivel,
    required int vacantes,
    required String salario,
    required bool activa,
  }) = _OfertaModel;

  factory OfertaModel.fromJson(Map<String, dynamic> json) => _$OfertaModelFromJson(json);

  factory OfertaModel.fromEntity(Oferta entity) => OfertaModel(
    id: entity.id,
    empresaId: entity.empresaId,
    titulo: entity.titulo,
    descripcion: entity.descripcion,
    ubicacion: entity.ubicacion,
    estudiosMinimos: entity.estudiosMinimos,
    experienciaMinima: entity.experienciaMinima,
    idiomas: entity.idiomas,
    nivel: entity.nivel,
    vacantes: entity.vacantes,
    salario: entity.salario,
    activa: entity.activa,
  );

  Oferta toEntity() => Oferta(
    id: id,
    empresaId: empresaId,
    titulo: titulo,
    descripcion: descripcion,
    ubicacion: ubicacion,
    estudiosMinimos: estudiosMinimos,
    experienciaMinima: experienciaMinima,
    idiomas: idiomas,
    nivel: nivel,
    vacantes: vacantes,
    salario: salario,
    activa: activa,
  );
}
