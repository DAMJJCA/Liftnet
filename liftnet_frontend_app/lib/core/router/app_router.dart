import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../../features/feed/presentation/screens/feed_screen.dart';
import '../../features/auth/presentation/screens/login_screen.dart';
import '../../features/auth/presentation/screens/register_screen.dart';
import '../../features/profile/presentation/screens/profile_screen.dart';
import '../../features/company_offers/presentation/screens/mis_ofertas_screen.dart';
import '../../features/company_offers/presentation/screens/crear_oferta_screen.dart';
import '../../features/feed/domain/entities/oferta.dart';
import '../../features/feed/presentation/screens/oferta_detail_screen.dart';

// Provider para leer el rol del usuario de forma síncrona/reactiva
final userRoleProvider = FutureProvider<String?>((ref) async {
  const storage = FlutterSecureStorage();
  return await storage.read(key: 'user_role');
});

final routerProvider = Provider<GoRouter>((ref) {
  final roleAsync = ref.watch(userRoleProvider);

  return GoRouter(
    initialLocation: '/login',
    routes: [
      GoRoute(
        path: '/login',
        builder: (context, state) => const LoginScreen(),
      ),
      GoRoute(
        path: '/register',
        builder: (context, state) => const RegisterScreen(),
      ),
      GoRoute(
        path: '/crear-oferta',
        builder: (context, state) => const CrearOfertaScreen(),
      ),
      GoRoute(
        path: '/oferta-detalle',
        builder: (context, state) => OfertaDetailScreen(oferta: state.extra as Oferta),
      ),
      ShellRoute(
        builder: (context, state, child) => MainScaffold(child: child),
        routes: [
          GoRoute(
            path: '/feed',
            builder: (context, state) {
              // Si es empresa, mostrar sus ofertas, si es postulante, el feed general
              return roleAsync.when(
                data: (role) => role == 'EMPRESA' ? const MisOfertasScreen() : const FeedScreen(),
                loading: () => const Scaffold(body: Center(child: CircularProgressIndicator())),
                error: (_, __) => const FeedScreen(),
              );
            },
          ),
          GoRoute(
            path: '/postulaciones',
            builder: (context, state) => const PlaceholderScreen('Mis Postulaciones'),
          ),
          GoRoute(
            path: '/perfil',
            builder: (context, state) => const ProfileScreen(),
          ),
        ],
      ),
    ],
  );
});

class MainScaffold extends StatelessWidget {
  final Widget child;
  const MainScaffold({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: child,
      bottomNavigationBar: NavigationBar(
        selectedIndex: _calculateSelectedIndex(context),
        onDestinationSelected: (index) => _onItemTapped(index, context),
        destinations: const [
          NavigationDestination(icon: Icon(Icons.work_outline), label: 'Ofertas'),
          NavigationDestination(icon: Icon(Icons.assignment_outlined), label: 'Postulaciones'),
          NavigationDestination(icon: Icon(Icons.person_outline), label: 'Perfil'),
        ],
      ),
    );
  }

  static int _calculateSelectedIndex(BuildContext context) {
    final String location = GoRouterState.of(context).uri.path;
    if (location.startsWith('/feed')) return 0;
    if (location.startsWith('/postulaciones')) return 1;
    if (location.startsWith('/perfil')) return 2;
    return 0;
  }

  void _onItemTapped(int index, BuildContext context) {
    switch (index) {
      case 0: context.go('/feed'); break;
      case 1: context.go('/postulaciones'); break;
      case 2: context.go('/perfil'); break;
    }
  }
}
