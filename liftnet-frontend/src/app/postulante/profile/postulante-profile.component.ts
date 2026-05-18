import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { UiStateService } from '../../core/services/ui-state.service';
import { PostulanteProfileStore } from '../../core/stores/postulante-profile.store';
import { SupabaseService } from '../../core/services/supabase.service';

@Component({
  selector: 'app-postulante-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './postulante-profile.component.html',
  styleUrls: ['./postulante-profile.component.css']
})
export class PostulanteProfileComponent {

  public store = inject(PostulanteProfileStore);
  private uiState = inject(UiStateService);
  private supabaseService = inject(SupabaseService);

  mandatoryMessage: string | null = null;
  isUploading = signal(false);
  subiendoCv = signal(false); // <-- ESTADO PARA EL CV

  constructor() {
    this.mandatoryMessage = this.uiState.getProfileMessage();
    this.uiState.clearProfileMessage();
    this.store.cargarTodo();
  }

  // --- LÓGICA DE SUBIDA DE AVATAR ---
  async onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;

    try {
      this.isUploading.set(true);
      const url = await this.supabaseService.uploadImage(file);
      const currentProfile = this.store.profile();
      this.store.profile.set({ ...currentProfile, fotoUrl: url });
      // Guardar el perfil en BD para que se registre la URL
      this.store.guardarDatosBasicos();
    } catch (err) {
      console.error('Error al subir la imagen:', err);
    } finally {
      this.isUploading.set(false);
    }
  }

  // --- LÓGICA DEL CURRÍCULUM PDF ---
  async onCvSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;

    if (file.type !== 'application/pdf') {
      alert('Por favor, selecciona un archivo PDF válido.');
      event.target.value = ''; // Resetea el input
      return;
    }

    this.subiendoCv.set(true);
    try {
      // 1. Subimos al bucket 'cv' en Supabase
      const url = await this.supabaseService.uploadDocument(file, 'cv');

      // 2. Lo asignamos al perfil temporalmente en Angular (¡Sin guardar en la BD todavía!)
      const currentProfile = this.store.profile();
      this.store.profile.set({ ...currentProfile, cvUrl: url });

      // 3. Avisamos al usuario para que guarde el formulario
      alert('PDF subido a la nube. ¡No olvides pulsar el botón verde "Guardar perfil" abajo del todo para guardar los cambios en la base de datos!');

    } catch (error) {
      alert('Error al subir el documento a la nube.');
    } finally {
      this.subiendoCv.set(false);
    }
  }

  async eliminarCv(cvUrl: string) {
    if (!confirm('¿Seguro que deseas eliminar tu currículum?')) return;

    this.subiendoCv.set(true);
    try {
      // 1. Borramos de Supabase
      await this.supabaseService.deleteDocument(cvUrl, 'cv');

      // 2. Actualizamos BD
      const currentProfile = this.store.profile();
      this.store.profile.set({ ...currentProfile, cvUrl: '' as any });
      this.store.guardarDatosBasicos();

    } catch (error) {
      alert('Error al eliminar el CV');
    } finally {
      this.subiendoCv.set(false);
    }
  }

  activarEdicion(): void {
    this.store.mode.set('edit');
  }

  cancelarEdicion(): void {
    this.store.mode.set('view');
    this.store.cargarTodo();
  }

  saveProfile(): void {
    this.store.guardarDatosBasicos();
  }
}
