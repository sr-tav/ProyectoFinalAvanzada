import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { AuthService } from '../../../core/services/auth.service';
import { TipoSolicitud, CanalOrigen, SugerenciaIAResponse } from '../../../core/models/models';

@Component({
  selector: 'app-crear-solicitud',
  templateUrl: './crear-solicitud.component.html',
  styleUrls: ['./crear-solicitud.component.scss'],
})
export class CrearSolicitudComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  loadingIA = false;
  success = false;
  error = '';
  sugerenciaIA: SugerenciaIAResponse | null = null;

  tipos = Object.values(TipoSolicitud);
  canales = Object.values(CanalOrigen);

  tipoLabels: Record<string, string> = {
    REGISTRO_ASIGNATURAS: 'Registro de Asignaturas',
    HOMOLOGACION: 'Homologación',
    CANCELACION_ASIGNATURAS: 'Cancelación de Asignaturas',
    SOLICITUD_CUPOS: 'Solicitud de Cupos',
    CONSULTA_ACADEMICA: 'Consulta Académica',
  };

  canalLabels: Record<string, string> = {
    CSU: 'CSU', CORREO: 'Correo', SAC: 'SAC', TELEFONICO: 'Telefónico', PRESENCIAL: 'Presencial',
  };

  constructor(
    private fb: FormBuilder,
    private solicitudService: SolicitudService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      tipo: ['', Validators.required],
      descripcion: ['', [Validators.required, Validators.minLength(10)]],
      canalOrigen: ['', Validators.required],
    });
  }

  sugerirIA(): void {
    const desc = this.form.get('descripcion')?.value;
    if (!desc || desc.length < 10) return;
    this.loadingIA = true;
    this.solicitudService.sugerir({ descripcion: desc }).subscribe({
      next: (res) => {
        this.sugerenciaIA = res;
        this.loadingIA = false;
      },
      error: () => { this.loadingIA = false; },
    });
  }

  aplicarSugerencia(): void {
    if (!this.sugerenciaIA) return;
    const tipo = this.sugerenciaIA.tipoSugerido;
    this.sugerenciaIA = null;
    setTimeout(() => this.form.get('tipo')?.setValue(tipo));
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    const solicitanteId = this.authService.getUsuarioId()!;

    this.solicitudService.registrar({ ...this.form.value, solicitanteId }).subscribe({
      next: () => { this.loading = false; this.success = true; this.form.reset(); },
      error: (err) => { this.loading = false; this.error = err.error?.message ?? 'Error al registrar la solicitud.'; },
    });
  }
}
