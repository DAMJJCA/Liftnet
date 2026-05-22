import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../providers/company_offers_provider.dart';

class CrearOfertaScreen extends ConsumerStatefulWidget {
  const CrearOfertaScreen({super.key});

  @override
  ConsumerState<CrearOfertaScreen> createState() => _CrearOfertaScreenState();
}

class _CrearOfertaScreenState extends ConsumerState<CrearOfertaScreen> {
  final _formKey = GlobalKey<FormState>();
  final _tituloController = TextEditingController();
  final _descripcionController = TextEditingController();
  final _ubicacionController = TextEditingController();
  final _estudiosController = TextEditingController();
  final _experienciaController = TextEditingController();
  final _idiomasController = TextEditingController();
  final _nivelController = TextEditingController();
  final _vacantesController = TextEditingController(text: '1');
  final _salarioController = TextEditingController();

  void _submit() async {
    if (!_formKey.currentState!.validate()) return;

    final ofertaData = {
      'titulo': _tituloController.text,
      'descripcion': _descripcionController.text,
      'ubicacion': _ubicacionController.text,
      'estudiosMinimos': _estudiosController.text,
      'experienciaMinima': _experienciaController.text,
      'idiomas': _idiomasController.text,
      'nivel': _nivelController.text,
      'vacantes': int.tryParse(_vacantesController.text) ?? 1,
      'salario': _salarioController.text,
    };

    try {
      await ref.read(companyOffersNotifierProvider.notifier).crearOferta(ofertaData);
      if (mounted) {
        context.pop();
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Oferta publicada con éxito')),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error: ${e.toString()}')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(companyOffersNotifierProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('Publicar Nueva Oferta')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _tituloController,
                decoration: const InputDecoration(labelText: 'Título de la vacante', border: OutlineInputBorder()),
                validator: (v) => v?.isEmpty ?? true ? 'Requerido' : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _descripcionController,
                decoration: const InputDecoration(labelText: 'Descripción detallada', border: OutlineInputBorder()),
                maxLines: 4,
                validator: (v) => v?.isEmpty ?? true ? 'Requerido' : null,
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _ubicacionController,
                      decoration: const InputDecoration(labelText: 'Ubicación', border: OutlineInputBorder()),
                      validator: (v) => v?.isEmpty ?? true ? 'Requerido' : null,
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: TextFormField(
                      controller: _salarioController,
                      decoration: const InputDecoration(labelText: 'Salario', border: OutlineInputBorder()),
                      validator: (v) => v?.isEmpty ?? true ? 'Requerido' : null,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _estudiosController,
                decoration: const InputDecoration(labelText: 'Estudios mínimos', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _experienciaController,
                decoration: const InputDecoration(labelText: 'Experiencia mínima', border: OutlineInputBorder()),
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _nivelController,
                      decoration: const InputDecoration(labelText: 'Nivel del puesto', border: OutlineInputBorder()),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: TextFormField(
                      controller: _vacantesController,
                      decoration: const InputDecoration(labelText: 'Vacantes', border: OutlineInputBorder()),
                      keyboardType: TextInputType.number,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 32),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: state.isLoading ? null : _submit,
                  style: ElevatedButton.styleFrom(padding: const EdgeInsets.all(16)),
                  child: state.isLoading 
                    ? const CircularProgressIndicator()
                    : const Text('Publicar Oferta'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
