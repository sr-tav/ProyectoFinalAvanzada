import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RolUsuario } from '../../core/models/models';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  @Input() title = 'Triage Académico';

  RolUsuario = RolUsuario;

  constructor(public authService: AuthService, private router: Router) {}

  get rol(): RolUsuario | null {
    return this.authService.getRol();
  }

  get rolLabel(): string {
    const map: Record<string, string> = {
      ESTUDIANTE: 'Estudiante',
      FUNCIONARIO: 'Funcionario',
      COORDINADOR: 'Coordinador',
      ADMIN: 'Administrador',
    };
    return map[this.rol ?? ''] ?? this.rol ?? '';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }
}
