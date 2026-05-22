import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../../core/network/dio_client.dart';
import '../../data/repositories/auth_repository_impl.dart';

final authRepositoryProvider = Provider((ref) {
  final dio = ref.watch(dioProvider);
  return AuthRepository(dio);
});

final authStateProvider = StateNotifierProvider<AuthNotifier, bool>((ref) {
  return AuthNotifier(ref.watch(authRepositoryProvider));
});

class AuthNotifier extends StateNotifier<bool> {
  final AuthRepository _repository;
  AuthNotifier(this._repository) : super(false) {
    checkAuthStatus();
  }

  Future<void> checkAuthStatus() async {
    final token = await _repository.getToken();
    if (token != null) {
      state = true;
    }
  }

  Future<void> login(String email, String password) async {
    try {
      await _repository.login(email, password);
      state = true;
    } catch (e) {
      state = false;
      rethrow;
    }
  }

  Future<void> register({
    required String email,
    required String password,
    required String nombre,
    required String role,
  }) async {
    try {
      await _repository.register(
        email: email,
        password: password,
        nombre: nombre,
        role: role,
      );
      state = true;
    } catch (e) {
      state = false;
      rethrow;
    }
  }

  Future<void> logout() async {
    await _repository.logout();
    state = false;
  }
}
