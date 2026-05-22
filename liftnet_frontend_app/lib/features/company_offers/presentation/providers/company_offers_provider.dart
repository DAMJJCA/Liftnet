import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../../core/network/dio_client.dart';
import '../../data/repositories/company_repository_impl.dart';
import '../../../feed/domain/entities/oferta.dart';

final companyRepositoryProvider = Provider((ref) {
  final dio = ref.watch(dioProvider);
  return CompanyRepository(dio);
});

final misOfertasProvider = FutureProvider<List<Oferta>>((ref) async {
  final repository = ref.watch(companyRepositoryProvider);
  return repository.getMisOfertas();
});

final companyOffersNotifierProvider = StateNotifierProvider<CompanyOffersNotifier, AsyncValue<void>>((ref) {
  return CompanyOffersNotifier(ref.watch(companyRepositoryProvider), ref);
});

class CompanyOffersNotifier extends StateNotifier<AsyncValue<void>> {
  final CompanyRepository _repository;
  final Ref _ref;

  CompanyOffersNotifier(this._repository, this._ref) : super(const AsyncValue.data(null));

  Future<void> crearOferta(Map<String, dynamic> ofertaData) async {
    state = const AsyncValue.loading();
    try {
      await _repository.crearOferta(ofertaData);
      state = const AsyncValue.data(null);
      // Refrescar la lista de mis ofertas
      _ref.invalidate(misOfertasProvider);
    } catch (e, stack) {
      state = AsyncValue.error(e, stack);
      rethrow;
    }
  }

  Future<void> cerrarOferta(String id) async {
    state = const AsyncValue.loading();
    try {
      await _repository.cerrarOferta(id);
      state = const AsyncValue.data(null);
      _ref.invalidate(misOfertasProvider);
    } catch (e, stack) {
      state = AsyncValue.error(e, stack);
      rethrow;
    }
  }
}
