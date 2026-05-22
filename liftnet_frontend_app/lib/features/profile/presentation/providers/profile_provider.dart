import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../../core/network/dio_client.dart';
import '../../../../core/network/storage_service.dart';
import '../../data/repositories/profile_repository_impl.dart';

// Provider para el servicio de almacenamiento
final storageServiceProvider = Provider((ref) => StorageService());

// Provider para el repositorio de perfil
final profileRepositoryProvider = Provider((ref) {
  final dio = ref.watch(dioProvider);
  return ProfileRepository(dio);
});

// StateNotifier para gestionar la lógica de subida y actualización del perfil
final profileNotifierProvider = StateNotifierProvider<ProfileNotifier, ProfileState>((ref) {
  return ProfileNotifier(
    ref.watch(profileRepositoryProvider),
    ref.watch(storageServiceProvider),
  );
});

class ProfileState {
  final bool isLoading;
  final String? error;
  final String? cvUrl;
  final String? fotoUrl;

  ProfileState({
    this.isLoading = false,
    this.error,
    this.cvUrl,
    this.fotoUrl,
  });

  ProfileState copyWith({
    bool? isLoading,
    String? error,
    String? cvUrl,
    String? fotoUrl,
  }) {
    return ProfileState(
      isLoading: isLoading ?? this.isLoading,
      error: error,
      cvUrl: cvUrl ?? this.cvUrl,
      fotoUrl: fotoUrl ?? this.fotoUrl,
    );
  }
}

class ProfileNotifier extends StateNotifier<ProfileState> {
  final ProfileRepository _repository;
  final StorageService _storageService;

  ProfileNotifier(this._repository, this._storageService) : super(ProfileState());

  Future<void> uploadAndUpdateCV() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final url = await _storageService.uploadCV();
      if (url != null) {
        await _repository.actualizarCvUrl(url);
        state = state.copyWith(isLoading: false, cvUrl: url);
      } else {
        state = state.copyWith(isLoading: false);
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }

  Future<void> uploadAndUpdateAvatar() async {
    state = state.copyWith(isLoading: true, error: null);
    try {
      final url = await _storageService.uploadAvatar();
      if (url != null) {
        await _repository.actualizarFotoUrl(url);
        state = state.copyWith(isLoading: false, fotoUrl: url);
      } else {
        state = state.copyWith(isLoading: false);
      }
    } catch (e) {
      state = state.copyWith(isLoading: false, error: e.toString());
    }
  }
}
