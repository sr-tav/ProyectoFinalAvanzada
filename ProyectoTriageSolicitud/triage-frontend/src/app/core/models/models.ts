// ──────────────────────────────────────────────
// Enums — deben coincidir exactamente con el backend
// ──────────────────────────────────────────────

export enum TipoSolicitud {
  REGISTRO_ASIGNATURAS = 'REGISTRO_ASIGNATURAS',
  HOMOLOGACION = 'HOMOLOGACION',
  CANCELACION_ASIGNATURAS = 'CANCELACION_ASIGNATURAS',
  SOLICITUD_CUPOS = 'SOLICITUD_CUPOS',
  CONSULTA_ACADEMICA = 'CONSULTA_ACADEMICA',
}

export enum CanalOrigen {
  CSU = 'CSU',
  CORREO = 'CORREO',
  SAC = 'SAC',
  TELEFONICO = 'TELEFONICO',
  PRESENCIAL = 'PRESENCIAL',
}

export enum EstadoSolicitud {
  REGISTRADA = 'REGISTRADA',
  CLASIFICADA = 'CLASIFICADA',
  EN_ATENCION = 'EN_ATENCION',
  ATENDIDA = 'ATENDIDA',
  CERRADA = 'CERRADA',
}

export enum Prioridad {
  CRITICA = 'CRITICA',
  ALTA = 'ALTA',
  MEDIA = 'MEDIA',
  BAJA = 'BAJA',
}

export enum RolUsuario {
  ESTUDIANTE = 'ESTUDIANTE',
  FUNCIONARIO = 'FUNCIONARIO',
  COORDINADOR = 'COORDINADOR',
  ADMIN = 'ADMIN',
}

// ──────────────────────────────────────────────
// Response DTOs
// ──────────────────────────────────────────────

export interface SolicitudResponse {
  id: number;
  tipo: TipoSolicitud;
  descripcion: string;
  canalOrigen: CanalOrigen;
  fechaRegistro: string;
  solicitanteId: number;
  estado: EstadoSolicitud;
  prioridad: Prioridad;
  responsableId: number | null;
  observaciones: string | null;
  version: number;
}

export interface HistorialItemResponse {
  fecha: string;
  accion: string;
  usuario: string;
  observaciones: string | null;
}

export interface SugerenciaIAResponse {
  tipoSugerido: TipoSolicitud;
  prioridadSugerida: Prioridad;
  confianza: number;
  explicacion: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  usuarioId: number;
  rol: RolUsuario;
}

// ──────────────────────────────────────────────
// Request DTOs
// ──────────────────────────────────────────────

export interface LoginRequest {
  correo: string;
}

export interface SolicitudRequest {
  tipo: TipoSolicitud;
  descripcion: string;
  canalOrigen: CanalOrigen;
  solicitanteId: number;
}

export interface ClasificarRequest {
  tipo: TipoSolicitud;
  version: number;
}

export interface PriorizarRequest {
  prioridad: Prioridad;
  justificacion: string;
  version: number;
}

export interface AsignarRequest {
  responsableId: number;
  version: number;
}

export interface AtenderRequest {
  observacion: string;
  version: number;
}

export interface CerrarRequest {
  observacion: string;
  version: number;
}

export interface VersionRequest {
  version: number;
}

export interface SugerenciaIARequest {
  descripcion: string;
}
