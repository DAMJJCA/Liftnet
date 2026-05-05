import { Injectable, signal, computed } from '@angular/core';

const TOKEN_KEY = 'liftnet_token';
const ROLE_KEY = 'liftnet_role';
const EMAIL_KEY = 'liftnet_email';
const PROFILE_COMPLETED_KEY = 'liftnet_profile_completed';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  // 1. Signals privados (leemos del localStorage al iniciar la app)
  private readonly _token = signal<string | null>(localStorage.getItem(TOKEN_KEY));
  private readonly _role = signal<string | null>(localStorage.getItem(ROLE_KEY));
  private readonly _email = signal<string | null>(localStorage.getItem(EMAIL_KEY));
  private readonly _profileCompleted = signal<boolean>(localStorage.getItem(PROFILE_COMPLETED_KEY) === 'true');

  // 2. Computed Properties (Exponemos estado reactivo y seguro de solo lectura)
  readonly isLoggedIn = computed(() => !!this._token());
  readonly currentRole = computed(() => this._role());
  readonly currentEmail = computed(() => this._email());
  readonly isProfileCompleted = computed(() => this._profileCompleted());

  // ---------- MÉTODOS DE ESCRITURA ----------

  saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
    this._token.set(token);
  }

  saveRole(role: string | null): void {
    if (role) {
      localStorage.setItem(ROLE_KEY, role);
      this._role.set(role);
    } else {
      localStorage.removeItem(ROLE_KEY);
      this._role.set(null);
    }
  }

  saveEmail(email: string): void {
    localStorage.setItem(EMAIL_KEY, email);
    this._email.set(email);
  }

  saveProfileCompleted(value: boolean): void {
    localStorage.setItem(PROFILE_COMPLETED_KEY, value ? 'true' : 'false');
    this._profileCompleted.set(value);
  }

  // Compatibilidad hacia atrás (para no romper tus guards actuales inmediatamente)
  getToken(): string | null { return this._token(); }
  getRole(): string | null { return this._role(); }
  getEmail(): string | null { return this._email(); }

  clear(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(EMAIL_KEY);
    localStorage.removeItem(PROFILE_COMPLETED_KEY);

    this._token.set(null);
    this._role.set(null);
    this._email.set(null);
    this._profileCompleted.set(false);
  }
}
