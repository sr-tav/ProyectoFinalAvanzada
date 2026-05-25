import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RolUsuario } from '../../../core/models/models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';

    this.authService.login(this.form.value).subscribe({
      next: (res) => {
        this.loading = false;
        this.redirect(res.rol);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message ?? 'Correo inválido o usuario inactivo.';
      },
    });
  }

  private redirect(rol: RolUsuario): void {
    switch (rol) {
      case RolUsuario.ESTUDIANTE:    this.router.navigate(['/usuario/mis-solicitudes']); break;
      case RolUsuario.FUNCIONARIO:   this.router.navigate(['/funcionario/solicitudes']); break;
      case RolUsuario.ADMIN:
      case RolUsuario.COORDINADOR:   this.router.navigate(['/admin/solicitudes']); break;
      default:                       this.router.navigate(['/auth/login']);
    }
  }
}
