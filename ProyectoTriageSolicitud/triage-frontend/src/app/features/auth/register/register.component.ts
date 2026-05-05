import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import {RolUsuario} from "../../../core/models/models";
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  form: FormGroup;
  submitted = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  )
  {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      identificacion: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.submitted = true;
    this.error = '';

    this.authService.register(this.form.value).subscribe({
      next: (res) => {
        this.submitted = false;
        this.redirect(res.rol);
      },
      error: (err) => {
        this.submitted = false;
        this.error = err.error?.message ?? 'Correo inválido o usuario inactivo.';
      },
    });
    if (this.form.valid) this.submitted = true;
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
