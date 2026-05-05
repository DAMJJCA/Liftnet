import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';
import { TokenStorageService } from '../../core/services/token-storage.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  private authService = inject(AuthService);
  private tokenStorage = inject(TokenStorageService);

  // Conectamos los signals directamente a la vista
  isLoggedIn = this.tokenStorage.isLoggedIn;
  isProfileCompleted = this.tokenStorage.isProfileCompleted;
  role = this.tokenStorage.currentRole;

  logout(): void {
    this.authService.logout();
  }
}
