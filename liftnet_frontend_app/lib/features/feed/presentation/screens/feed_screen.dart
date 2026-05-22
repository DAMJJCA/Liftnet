import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../providers/ofertas_provider.dart';
import '../widgets/oferta_card.dart';

class FeedScreen extends ConsumerWidget {
  const FeedScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final ofertasAsync = ref.watch(ofertasActivasProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Liftnet - Ofertas'),
        centerTitle: false,
        actions: [
          IconButton(
            icon: const Icon(Icons.filter_list),
            onPressed: () {
              // TODO: Implementar filtros por ubicación
            },
          ),
        ],
      ),
      body: ofertasAsync.when(
        data: (ofertas) {
          if (ofertas.isEmpty) {
            return const Center(
              child: Text('No hay ofertas disponibles por el momento.'),
            );
          }
          return ListView.builder(
            itemCount: ofertas.length,
            itemBuilder: (context, index) {
              final oferta = ofertas[index];
              return OfertaCard(
                oferta: oferta,
                onTap: () {
                  // TODO: Navegar al detalle de la oferta
                },
              );
            },
          );
        },
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.error_outline, color: Colors.red, size: 48),
              const SizedBox(height: 16),
              Text('Error: ${error.toString()}'),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () => ref.refresh(ofertasActivasProvider),
                child: const Text('Reintentar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
