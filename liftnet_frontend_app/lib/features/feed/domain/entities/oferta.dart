class Oferta {
  final String id;
  final String empresaId;
  final String titulo;
  final String descripcion;
  final String ubicacion;
  final DateTime? fechaInicio;
  final DateTime? fechaFin;
  final bool activa;
  final String estudiosMinimos;
  final String experienciaMinima;
  final String idiomas;
  final String nivel;
  final int vacantes;
  final String salario;

  Oferta({
    required this.id,
    required this.empresaId,
    required this.titulo,
    required this.descripcion,
    required this.ubicacion,
    this.fechaInicio,
    this.fechaFin,
    required this.activa,
    required this.estudiosMinimos,
    required this.experienciaMinima,
    required this.idiomas,
    required this.nivel,
    required this.vacantes,
    required this.salario,
  });
}
