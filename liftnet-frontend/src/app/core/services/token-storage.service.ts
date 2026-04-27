import { Injectable } from '@angular/core';

const TOKEN_KEY = 'liftnet_token';
const ROLE_KEY = 'liftnet_role';
const EMAIL_KEY = 'liftnet_email';
const PROFILE_COMPLETED_KEY = 'liftnet_profile_completed';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  // ---------- TOKEN ----------
  saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  // ---------- ROLE ----------
  saveRole(role: string): void {
    localStorage.setItem(ROLE_KEY, role);
  }

  getRole(): string | null {
    return localStorage.getItem(ROLE_KEY);
  }

  // ---------- EMAIL ----------
  saveEmail(email: string): void {
    localStorage.setItem(EMAIL_KEY, email);
  }

  getEmail(): string | null {
    return localStorage.getItem(EMAIL_KEY);
  }

  // ---------- PROFILE COMPLETED ----------

saveProfileCompleted(value: boolean): void {
  localStorage.setItem(PROFILE_COMPLETED_KEY, value ? 'true' : 'false');
}

isProfileCompleted(): boolean {
  return localStorage.getItem(PROFILE_COMPLETED_KEY) === 'true';
}


  // ---------- GENERAL ----------
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  clear(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(EMAIL_KEY);
    localStorage.removeItem(PROFILE_COMPLETED_KEY);
  }
}
