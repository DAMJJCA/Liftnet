import '../entities/oferta.dart';

abstract class OfertasRepository {
  Future<List<Oferta>> getOfertasActivas({String? ubicacion});
}
