import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../providers/company_offers_provider.dart';
import '../../../feed/presentation/widgets/oferta_card.dart';

class MisOfertasScreen extends ConsumerWidget {
  const MisOfertasScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final misOfertasAsync = ref.watch(misOfertasProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Mis Ofertas Publicadas'),
      ),
      body: misOfertasAsync.when(
        data: (ofertas) {
          if (ofertas.isEmpty) {
            return const Center(child: Text('Aún no has publicado ninguna oferta.'));
          }
          return ListView.builder(
            itemCount: ofertas.length,
            itemBuilder: (context, index) {
              final oferta = ofertas[index];
              return OfertaCard(
                oferta: oferta,
                onTap: () {
                  // Navegar a gestión de candidatos para esta oferta
                  context.push('/oferta-candidatos', extra: oferta);
                },
              );
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, s) => Center(child: Text('Error: $e')),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.push('/crear-oferta'),
        label: const Text('Nueva Oferta'),
        icon: const Icon(Icons.add),
      ),
    );
  }
}
