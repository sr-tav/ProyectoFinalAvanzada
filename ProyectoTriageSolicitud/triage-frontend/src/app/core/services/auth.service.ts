import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import {CuentaResponse, LoginRequest, LoginResponse, RegisterResponse, RolUsuario} from '../models/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly BASE = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.BASE}/login`, body).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('rol', res.rol);
        localStorage.setItem('usuarioId', String(res.usuarioId));
      })
    );
  }

  register(body: RegisterResponse): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.BASE}/register`, body).pipe(
      tap((res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('rol', res.rol);
        localStorage.setItem('usuarioId', String(res.usuarioId));
      })
    );
  }

  obtenerCuenta(): Observable<CuentaResponse> {
    const usuarioId = this.getUsuarioId();

    return this.http.get<CuentaResponse>(`${this.BASE}/cuenta/${usuarioId}`).pipe(
      tap((res) => {
        localStorage.setItem('nombre', res.nombre);
        localStorage.setItem('correo', res.correo);
        localStorage.setItem('estado', res.estado);
        localStorage.setItem('rol', res.rol);
        localStorage.setItem('usuarioId', String(res.usuarioId));
      })
    );
  }

  logout(): void {
    localStorage.clear();
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRol(): RolUsuario | null {
    return localStorage.getItem('rol') as RolUsuario | null;
  }

  getUsuarioId(): number | null {
    const id = localStorage.getItem('usuarioId');
    return id ? Number(id) : null;
  }
  getNombre(): string | null {
    return localStorage.getItem('nombre');
  }
  getCorreo(): string | null {
    return localStorage.getItem('correo');
  }
  getEstado(): string | null {
    return localStorage.getItem('estado');
  }
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const rol = this.getRol();
    return rol === RolUsuario.ADMIN || rol === RolUsuario.COORDINADOR;
  }

  isFuncionario(): boolean {
    const rol = this.getRol();
    return rol === RolUsuario.FUNCIONARIO || this.isAdmin();
  }
}
