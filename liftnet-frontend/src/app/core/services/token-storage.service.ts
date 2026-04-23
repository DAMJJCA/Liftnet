import { Injectable } from '@angular/core';

const TOKEN_KEY = 'liftnet_token';
const ROLE_KEY = 'liftnet_role';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  saveRole(role: string): void {
    localStorage.setItem(ROLE_KEY, role);
  }

  getRole(): string | null {
    return localStorage.getItem(ROLE_KEY);
  }

  clear(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
