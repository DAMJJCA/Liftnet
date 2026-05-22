import 'dart:io';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'package:file_picker/file_picker.dart';
import '../constants/supabase_constants.dart';

class StorageService {
  final SupabaseClient _supabase = Supabase.instance.client;

  /// Permite al usuario seleccionar un archivo y subirlo al bucket especificado.
  /// Retorna la URL pública del archivo subido.
  Future<String?> uploadFile({
    required String bucketName,
    required String folderPath,
  }) async {
    try {
      // 1. Seleccionar el archivo
      FilePickerResult? result = await FilePicker.platform.pickFiles(
        type: FileType.custom,
        allowedExtensions: ['pdf', 'jpg', 'png', 'jpeg'],
      );

      if (result == null || result.files.single.path == null) return null;

      final file = File(result.files.single.path!);
      final fileName = DateTime.now().millisecondsSinceEpoch.toString() + '_' + result.files.single.name;
      final path = '$folderPath/$fileName';

      // 2. Subir a Supabase Storage
      await _supabase.storage.from(bucketName).upload(path, file);

      // 3. Obtener la URL pública
      final String publicUrl = _supabase.storage.from(bucketName).getPublicUrl(path);
      
      return publicUrl;
    } catch (e) {
      throw Exception('Error al subir archivo a Supabase: $e');
    }
  }

  /// Específicamente para el CV del postulante
  Future<String?> uploadCV() async {
    return await uploadFile(
      bucketName: SupabaseConstants.cvBucket,
      folderPath: 'cvs',
    );
  }

  /// Específicamente para la foto de perfil
  Future<String?> uploadAvatar() async {
    return await uploadFile(
      bucketName: SupabaseConstants.avatarBucket,
      folderPath: 'avatars',
    );
  }
}
