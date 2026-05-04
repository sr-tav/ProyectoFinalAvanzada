import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CrearSolicitudComponent } from './crear-solicitud/crear-solicitud.component';
import { MisSolicitudesComponent } from './mis-solicitudes/mis-solicitudes.component';

const routes: Routes = [
  { path: 'mis-solicitudes', component: MisSolicitudesComponent },
  { path: 'crear-solicitud', component: CrearSolicitudComponent },
  { path: '', redirectTo: 'mis-solicitudes', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardUsuarioRoutingModule {}
