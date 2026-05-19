import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { AuthService } from '../../../core/services/auth.service';
import { CuentaResponse } from '../../../core/models/models';

@Component({
  selector: 'app-mi-cuenta',
  standalone: true,
  imports: [CommonModule, SharedModule],
  templateUrl: './mi-cuenta.component.html',
  styleUrls: ['./mi-cuenta.component.scss']
})
export class MiCuentaComponent implements OnInit {

  isLoading = true;
  error = '';
  cuenta: CuentaResponse | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.cargarCuenta();
  }

  cargarCuenta(): void {
    this.isLoading = true;
    this.error = '';

    this.authService.obtenerCuenta().subscribe({
      next: (res) => {
        this.cuenta = res;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = err.error?.message ?? 'No se pudo cargar la información de la cuenta.';
        this.isLoading = false;
      }
    });
  }
}
