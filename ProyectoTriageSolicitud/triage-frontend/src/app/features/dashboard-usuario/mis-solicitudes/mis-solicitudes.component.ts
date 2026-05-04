import { Component, OnInit } from '@angular/core';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { AuthService } from '../../../core/services/auth.service';
import { SolicitudResponse, EstadoSolicitud } from '../../../core/models/models';

@Component({
  selector: 'app-mis-solicitudes',
  templateUrl: './mis-solicitudes.component.html',
  styleUrls: ['./mis-solicitudes.component.scss'],
})
export class MisSolicitudesComponent implements OnInit {
  solicitudes: SolicitudResponse[] = [];
  loading = true;
  error = '';
  selectedSolicitud: SolicitudResponse | null = null;

  tipoLabels: Record<string, string> = {
    REGISTRO_ASIGNATURAS: 'Registro', HOMOLOGACION: 'Homologación',
    CANCELACION_ASIGNATURAS: 'Cancelación', SOLICITUD_CUPOS: 'Cupos',
    CONSULTA_ACADEMICA: 'Consulta',
  };

  constructor(
    private solicitudService: SolicitudService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading = true;
    const responsableId = this.authService.getUsuarioId()!;
    // TODO: filtrar por solicitanteId cuando el backend lo soporte
    this.solicitudService.listar({ responsableId }).subscribe({
      next: (data) => { this.solicitudes = data; this.loading = false; },
      error: (err) => { this.error = err.error?.message ?? 'Error al cargar solicitudes.'; this.loading = false; },
    });
  }

  verDetalle(s: SolicitudResponse): void {
    this.selectedSolicitud = s;
  }

  cerrarDetalle(): void {
    this.selectedSolicitud = null;
  }

  badgeEstado(estado: string): string {
    return `badge badge-${estado.toLowerCase().replace('_', '-')}`;
  }

  badgePrioridad(p: string): string {
    return `badge badge-${p.toLowerCase()}`;
  }
}
