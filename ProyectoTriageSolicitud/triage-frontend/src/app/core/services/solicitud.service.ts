import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  SolicitudResponse, HistorialItemResponse, SugerenciaIAResponse,
  SolicitudRequest, ClasificarRequest, PriorizarRequest, AsignarRequest,
  AtenderRequest, CerrarRequest, VersionRequest, SugerenciaIARequest,
  EstadoSolicitud, TipoSolicitud, Prioridad
} from '../models/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SolicitudService {
  private readonly BASE = `${environment.apiUrl}/solicitudes`;

  constructor(private http: HttpClient) {}

  registrar(body: SolicitudRequest): Observable<SolicitudResponse> {
    return this.http.post<SolicitudResponse>(this.BASE, body);
  }

  listar(filters?: {
    estado?: EstadoSolicitud;
    tipo?: TipoSolicitud;
    responsableId?: number;
    prioridad?: Prioridad;
  }): Observable<SolicitudResponse[]> {
    let params = new HttpParams();
    if (filters?.estado)        params = params.set('estado', filters.estado);
    if (filters?.tipo)          params = params.set('tipo', filters.tipo);
    if (filters?.responsableId) params = params.set('responsableId', String(filters.responsableId));
    if (filters?.prioridad)     params = params.set('prioridad', filters.prioridad);
    return this.http.get<SolicitudResponse[]>(this.BASE, { params });
  }

  obtener(id: number): Observable<SolicitudResponse> {
    return this.http.get<SolicitudResponse>(`${this.BASE}/${id}`);
  }

  historial(id: number): Observable<HistorialItemResponse[]> {
    return this.http.get<HistorialItemResponse[]>(`${this.BASE}/${id}/historial`);
  }

  clasificar(id: number, body: ClasificarRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/clasificar`, body);
  }

  priorizar(id: number, body: PriorizarRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/priorizar`, body);
  }

  asignar(id: number, body: AsignarRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/asignar`, body);
  }

  iniciarAtencion(id: number, body: VersionRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/iniciar-atencion`, body);
  }

  atender(id: number, body: AtenderRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/atender`, body);
  }

  cerrar(id: number, body: CerrarRequest): Observable<SolicitudResponse> {
    return this.http.patch<SolicitudResponse>(`${this.BASE}/${id}/cerrar`, body);
  }

  sugerir(body: SugerenciaIARequest): Observable<SugerenciaIAResponse> {
    return this.http.post<SugerenciaIAResponse>(`${this.BASE}/sugerir`, body);
  }
}
