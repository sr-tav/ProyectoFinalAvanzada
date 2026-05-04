import { Component, OnInit } from '@angular/core';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { SolicitudResponse, TipoSolicitud, Prioridad, EstadoSolicitud } from '../../../core/models/models';

@Component({
  selector: 'app-solicitudes-funcionario',
  templateUrl: './solicitudes-funcionario.component.html',
  styleUrls: ['./solicitudes-funcionario.component.scss'],
})
export class SolicitudesFuncionarioComponent implements OnInit {
  solicitudes: SolicitudResponse[] = [];
  loading = true;
  error = '';

  // Filtros
  filtroTipo: TipoSolicitud | '' = '';
  filtroPrioridad: Prioridad | '' = '';

  // Modal responder
  solicitudSeleccionada: SolicitudResponse | null = null;

  tipos = Object.values(TipoSolicitud);
  prioridades = Object.values(Prioridad);

  tipoLabels: Record<string, string> = {
    REGISTRO_ASIGNATURAS: 'Registro Asignaturas', HOMOLOGACION: 'Homologación',
    CANCELACION_ASIGNATURAS: 'Cancelación', SOLICITUD_CUPOS: 'Cupos',
    CONSULTA_ACADEMICA: 'Consulta Académica',
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
        // Ordenar por prioridad: CRITICA > ALTA > MEDIA > BAJA
        const orden: Record<string, number> = { CRITICA: 0, ALTA: 1, MEDIA: 2, BAJA: 3 };
        this.solicitudes = data.sort((a, b) => (orden[a.prioridad] ?? 9) - (orden[b.prioridad] ?? 9));
        this.loading = false;
      },
      error: (err) => { this.error = err.error?.message ?? 'Error al cargar.'; this.loading = false; },
    });
  }

  aplicarFiltros(): void { this.cargar(); }
  limpiarFiltros(): void { this.filtroTipo = ''; this.filtroPrioridad = ''; this.cargar(); }

  abrirResponder(s: SolicitudResponse): void { this.solicitudSeleccionada = s; }
  cerrarResponder(): void { this.solicitudSeleccionada = null; this.cargar(); }

  badgeEstado(e: string): string { return `badge badge-${e.toLowerCase().replace('_','-')}`; }
  badgePrioridad(p: string): string { return `badge badge-${p.toLowerCase()}`; }
}
