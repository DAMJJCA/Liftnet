import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../core/network/dio_client.dart';
import '../../data/repositories/ofertas_repository_impl.dart';
import '../../domain/entities/oferta.dart';
import '../../domain/repositories/ofertas_repository.dart';

// Provider para el repositorio
final ofertasRepositoryProvider = Provider<OfertasRepository>((ref) {
  final dio = ref.watch(dioProvider);
  return OfertasRepositoryImpl(dio);
});

// FutureProvider para obtener las ofertas activas
final ofertasActivasProvider = FutureProvider<List<Oferta>>((ref) async {
  final repository = ref.watch(ofertasRepositoryProvider);
  return repository.getOfertasActivas();
});
