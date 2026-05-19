import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CrearSolicitudComponent } from './crear-solicitud/crear-solicitud.component';
import { MisSolicitudesComponent } from './mis-solicitudes/mis-solicitudes.component';
import { MiCuentaComponent } from './mi-cuenta/mi-cuenta.component';


const routes: Routes = [
  { path: 'mis-solicitudes', component: MisSolicitudesComponent },
  { path: 'crear-solicitud', component: CrearSolicitudComponent },
  { path: 'mi-cuenta', component: MiCuentaComponent },
  { path: '', redirectTo: 'mis-solicitudes', pathMatch: 'full' },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardUsuarioRoutingModule {}
