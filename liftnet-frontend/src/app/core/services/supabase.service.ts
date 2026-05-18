import { Injectable } from '@angular/core';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { environment } from '../../../environments/environment.development';

@Injectable({ providedIn: 'root' })
export class SupabaseService {
  private supabase: SupabaseClient;

  constructor() {
    this.supabase = createClient(environment.supabaseUrl, environment.supabaseKey);
  }

  // 1. SUBIR FOTOS (Por defecto usa el bucket 'avatars')
  async uploadImage(file: File): Promise<string> {
    const cleanFileName = file.name.replace(/[^a-zA-Z0-9.]/g, '_');
    const fileName = `perfil_${Date.now()}_${cleanFileName}`;

    const { error } = await this.supabase.storage.from('avatars').upload(fileName, file);
    if (error) throw error;

    const { data: publicUrl } = this.supabase.storage.from('avatars').getPublicUrl(fileName);
    return publicUrl.publicUrl;
  }

  // 2. SUBIR DOCUMENTOS PDF (Le pasamos el nombre de tu bucket: 'experiencia', 'certificados'...)
  async uploadDocument(file: File, bucketName: string): Promise<string> {
    const cleanFileName = file.name.replace(/[^a-zA-Z0-9.]/g, '_');
    const fileName = `doc_${Date.now()}_${cleanFileName}`;

    const { error } = await this.supabase.storage.from(bucketName).upload(fileName, file);
    if (error) throw error;

    const { data: publicUrl } = this.supabase.storage.from(bucketName).getPublicUrl(fileName);
    return publicUrl.publicUrl;
  }

  // 3. ELIMINAR ARCHIVOS (Le pasamos la URL y el bucket donde está)
  async deleteDocument(url: string, bucketName: string): Promise<void> {
    try {
      const path = url.split(`/${bucketName}/`)[1];
      if (!path) return;

      const { error } = await this.supabase.storage.from(bucketName).remove([path]);
      if (error) console.error('Error al borrar de Supabase:', error);
    } catch (e) {
      console.error('No se pudo procesar el borrado del archivo', e);
    }
  }
}
