import { Component, OnInit } from '@angular/core';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { SolicitudResponse, TipoSolicitud, Prioridad, EstadoSolicitud } from '../../../core/models/models';

@Component({
  selector: 'app-solicitudes-admin',
  templateUrl: './solicitudes-admin.component.html',
  styleUrls: ['./solicitudes-admin.component.scss'],
})
export class SolicitudesAdminComponent implements OnInit {
  solicitudes: SolicitudResponse[] = [];
  historialSolicitudes: SolicitudResponse[] = [];
  mostrarHistorial = false;
  loading = true;
  error = '';
  filtroTipo: TipoSolicitud | '' = '';
  filtroPrioridad: Prioridad | '' = '';
  filtroEstado: EstadoSolicitud | '' = '';
  solicitudSeleccionada: SolicitudResponse | null = null;

  tipos = Object.values(TipoSolicitud);
  prioridades = Object.values(Prioridad);
  estados = Object.values(EstadoSolicitud).filter(e => e !== EstadoSolicitud.CERRADA);

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
      next: (data) => {
        const orden: Record<string, number> = { CRITICA: 0, ALTA: 1, MEDIA: 2, BAJA: 3 };
        const sorted = data.sort((a, b) => (orden[a.prioridad] ?? 9) - (orden[b.prioridad] ?? 9));
        this.historialSolicitudes = sorted;
        let activas = sorted.filter(s => s.estado !== EstadoSolicitud.CERRADA);
        if (this.filtroEstado) {
          activas = activas.filter(s => s.estado === this.filtroEstado);
        }
        this.solicitudes = activas;
        this.loading = false;
      },
      error: (err) => { this.error = err.error?.message ?? 'Error.'; this.loading = false; },
    });
  }

  aplicarFiltros(): void { this.cargar(); }
  limpiarFiltros(): void { this.filtroTipo = ''; this.filtroPrioridad = ''; this.filtroEstado = ''; this.cargar(); }

  abrirGestionar(s: SolicitudResponse): void { this.solicitudSeleccionada = s; }
  cerrarGestionar(): void { this.solicitudSeleccionada = null; this.cargar(); }

  get stats() {
    return {
      total: this.solicitudes.length,
      criticas: this.solicitudes.filter(s => s.prioridad === 'CRITICA').length,
      enAtencion: this.solicitudes.filter(s => s.estado === EstadoSolicitud.EN_ATENCION).length,
      cerradas: this.historialSolicitudes.filter(s => s.estado === EstadoSolicitud.CERRADA).length,
    };
  }

  badgeEstado(e: string): string { return `badge badge-${e.toLowerCase().replace('_','-')}`; }
  badgePrioridad(p: string): string { return `badge badge-${p.toLowerCase()}`; }
}
