import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { RolUsuario } from './core/models/models';

const routes: Routes = [
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.module').then((m) => m.AuthModule),
  },
  {
    path: 'usuario',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: [RolUsuario.ESTUDIANTE] },
    loadChildren: () =>
      import('./features/dashboard-usuario/dashboard-usuario.module').then(
        (m) => m.DashboardUsuarioModule
      ),
  },
  {
    path: 'funcionario',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: [RolUsuario.FUNCIONARIO] },
    loadChildren: () =>
      import('./features/dashboard-funcionario/dashboard-funcionario.module').then(
        (m) => m.DashboardFuncionarioModule
      ),
  },
  {
    path: 'admin',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: [RolUsuario.ADMIN, RolUsuario.COORDINADOR] },
    loadChildren: () =>
      import('./features/dashboard-admin/dashboard-admin.module').then(
        (m) => m.DashboardAdminModule
      ),
  },
  { path: '**', redirectTo: 'auth/login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
