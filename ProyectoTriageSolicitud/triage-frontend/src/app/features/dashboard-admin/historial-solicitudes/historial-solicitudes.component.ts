import { Component, OnInit } from '@angular/core';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { SolicitudResponse, HistorialItemResponse, TipoSolicitud, Prioridad, EstadoSolicitud } from '../../../core/models/models';

@Component({
  selector: 'app-historial-solicitudes',
  templateUrl: './historial-solicitudes.component.html',
  styleUrls: ['./historial-solicitudes.component.scss'],
})
export class HistorialSolicitudesComponent implements OnInit {
  solicitudes: SolicitudResponse[] = [];
  loading = true;
  error = '';
  filtroTipo: TipoSolicitud | '' = '';
  filtroPrioridad: Prioridad | '' = '';

  historialMap: Record<number, HistorialItemResponse[]> = {};
  solicitudExpandida: number | null = null;
  loadingHistorial = false;

  tipos = Object.values(TipoSolicitud);
  prioridades = Object.values(Prioridad);

  tipoLabels: Record<string, string> = {
    REGISTRO_ASIGNATURAS: 'Registro', HOMOLOGACION: 'Homologación',
    CANCELACION_ASIGNATURAS: 'Cancelación', SOLICITUD_CUPOS: 'Cupos',
    CONSULTA_ACADEMICA: 'Consulta',
  };

  constructor(private solicitudService: SolicitudService) {}

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.loading = true;
    this.solicitudService.listar({
      tipo: this.filtroTipo || undefined,
      prioridad: this.filtroPrioridad || undefined,
    }).subscribe({
      next: (data) => { this.solicitudes = data; this.loading = false; },
      error: (err) => { this.error = err.error?.message ?? 'Error.'; this.loading = false; },
    });
  }

  aplicarFiltros(): void { this.cargar(); }
  limpiarFiltros(): void { this.filtroTipo = ''; this.filtroPrioridad = ''; this.cargar(); }

  toggleHistorial(s: SolicitudResponse): void {
    if (this.solicitudExpandida === s.id) {
      this.solicitudExpandida = null;
      return;
    }
    this.solicitudExpandida = s.id;
    if (!this.historialMap[s.id]) {
      this.loadingHistorial = true;
      this.solicitudService.historial(s.id).subscribe({
        next: (h) => { this.historialMap[s.id] = h; this.loadingHistorial = false; },
        error: () => { this.loadingHistorial = false; },
      });
    }
  }

  badgeEstado(e: string): string { return `badge badge-${e.toLowerCase().replace('_','-')}`; }
  badgePrioridad(p: string): string { return `badge badge-${p.toLowerCase()}`; }
}
