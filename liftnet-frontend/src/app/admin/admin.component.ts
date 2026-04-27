import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
  AdminService,
  UserAdmin
} from '../core/services/admin.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  users: UserAdmin[] = [];
  loading = true;
  errorMessage: string | null = null;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: res => {
        if (res.success) {
          this.users = res.data;
        }
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'No se pudieron cargar los usuarios';
        this.loading = false;
      }
    });
  }

  changeRole(user: UserAdmin, newRole: string): void {
    this.adminService.changeRole(user.id, newRole).subscribe({
      next: () => {
        user.role = newRole;
      }
    });
  }

  toggleEnabled(user: UserAdmin): void {
    this.adminService.setEnabled(user.id, !user.enabled).subscribe({
      next: () => {
        user.enabled = !user.enabled;
      }
    });
  }
}
