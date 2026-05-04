import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SolicitudService } from '../../../core/services/solicitud.service';
import { AuthService } from '../../../core/services/auth.service';
import {
  SolicitudResponse, HistorialItemResponse,
  TipoSolicitud, Prioridad, EstadoSolicitud
} from '../../../core/models/models';

@Component({
  selector: 'app-responder-solicitud',
  templateUrl: './responder-solicitud.component.html',
  styleUrls: ['./responder-solicitud.component.scss'],
})
export class ResponderSolicitudComponent implements OnInit {
  @Input() solicitud!: SolicitudResponse;
  @Output() cerrar = new EventEmitter<void>();

  historial: HistorialItemResponse[] = [];
  loadingHistorial = true;
  loading = false;
  error = '';
  success = '';

  accionActiva: 'clasificar' | 'priorizar' | 'asignar' | 'iniciar' | 'atender' | 'cerrar' | null = null;

  tipos = Object.values(TipoSolicitud);
  prioridades = Object.values(Prioridad);

  formClasificar!: FormGroup;
  formPriorizar!: FormGroup;
  formAsignar!: FormGroup;
  formAtender!: FormGroup;
  formCerrar!: FormGroup;

  tipoLabels: Record<string, string> = {
    REGISTRO_ASIGNATURAS: 'Registro', HOMOLOGACION: 'Homologación',
    CANCELACION_ASIGNATURAS: 'Cancelación', SOLICITUD_CUPOS: 'Cupos',
    CONSULTA_ACADEMICA: 'Consulta',
  };

  constructor(
    private fb: FormBuilder,
    private solicitudService: SolicitudService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.formClasificar = this.fb.group({ tipo: ['', Validators.required] });
    this.formPriorizar  = this.fb.group({ prioridad: ['', Validators.required], justificacion: ['', Validators.required] });
    this.formAsignar    = this.fb.group({ responsableId: ['', Validators.required] });
    this.formAtender    = this.fb.group({ observacion: ['', [Validators.required, Validators.minLength(5)]] });
    this.formCerrar     = this.fb.group({ observacion: ['', [Validators.required, Validators.minLength(5)]] });
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.loadingHistorial = true;
    this.solicitudService.historial(this.solicitud.id).subscribe({
      next: (h) => { this.historial = h; this.loadingHistorial = false; },
      error: () => { this.loadingHistorial = false; },
    });
  }

  puedeClasificar(): boolean { return this.solicitud.estado === EstadoSolicitud.REGISTRADA; }
  puedePriorizar(): boolean  { return this.solicitud.estado === EstadoSolicitud.CLASIFICADA; }
  puedeAsignar(): boolean    { return this.solicitud.estado === EstadoSolicitud.CLASIFICADA; }
  puedeIniciar(): boolean    { return this.solicitud.estado === EstadoSolicitud.CLASIFICADA; }
  puedeAtender(): boolean    { return this.solicitud.estado === EstadoSolicitud.EN_ATENCION; }
  puedeCerrar(): boolean     { return this.solicitud.estado === EstadoSolicitud.ATENDIDA; }

  ejecutar(accion: string): void {
    this.loading = true;
    this.error = '';
    this.success = '';
    const id = this.solicitud.id;
    const v = this.solicitud.version;

    const obs$ = (() => {
      switch (accion) {
        case 'clasificar': return this.solicitudService.clasificar(id, { ...this.formClasificar.value, version: v });
        case 'priorizar':  return this.solicitudService.priorizar(id, { ...this.formPriorizar.value, version: v });
        case 'asignar':    return this.solicitudService.asignar(id, { responsableId: Number(this.formAsignar.value.responsableId), version: v });
        case 'iniciar':    return this.solicitudService.iniciarAtencion(id, { version: v });
        case 'atender':    return this.solicitudService.atender(id, { ...this.formAtender.value, version: v });
        case 'cerrar':     return this.solicitudService.cerrar(id, { ...this.formCerrar.value, version: v });
        default:           throw new Error('Acción inválida');
      }
    })();

    obs$.subscribe({
      next: (updated) => {
        this.solicitud = updated;
        this.accionActiva = null;
        this.success = 'Acción realizada con éxito.';
        this.loading = false;
        this.cargarHistorial();
      },
      error: (err) => {
        this.error = err.error?.message ?? 'Error al ejecutar la acción.';
        this.loading = false;
      },
    });
  }

  badgeEstado(e: string): string { return `badge badge-${e.toLowerCase().replace('_','-')}`; }
  badgePrioridad(p: string): string { return `badge badge-${p.toLowerCase()}`; }
}
