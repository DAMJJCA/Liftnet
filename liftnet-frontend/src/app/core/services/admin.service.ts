import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment.development';
import { ApiResponse } from '../models/api-response.model';

export interface UserAdmin {
  id: string;
  email: string;
  role: string;
  enabled: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private readonly apiUrl = `${environment.apiUrl}/admin/usuarios`;

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<ApiResponse<UserAdmin[]>> {
    return this.http.get<ApiResponse<UserAdmin[]>>(this.apiUrl);
  }

  changeRole(userId: string, role: string): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(
      `${this.apiUrl}/${userId}/rol?role=${role}`,
      {}
    );
  }

  setEnabled(userId: string, enabled: boolean): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(
      `${this.apiUrl}/${userId}/enabled?enabled=${enabled}`,
      {}
    );
  }
}
