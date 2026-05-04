import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RolUsuario } from '../models/models';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRoles: RolUsuario[] = route.data['roles'] ?? [];
    const userRol = this.auth.getRol();
    if (userRol && requiredRoles.includes(userRol)) return true;
    this.router.navigate(['/auth/login']);
    return false;
  }
}
