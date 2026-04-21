import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: '<p>Probar conexión backend (revisar consola)</p>',
})
export class AppComponent implements OnInit {

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.login({
      email: 'empresa1@liftnet.com',
      password: '123456'
    }).subscribe({
      next: (res) => console.log('✅ Backend respondió:', res),
      error: (err) => console.error('❌ Error:', err)
    });
  }
}
