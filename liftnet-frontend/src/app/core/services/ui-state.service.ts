import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UiStateService {

  private profileMessage: string | null = null;

  setProfileMessage(message: string): void {
    this.profileMessage = message;
  }

  getProfileMessage(): string | null {
    return this.profileMessage;
  }

  clearProfileMessage(): void {
    this.profileMessage = null;
  }
}
