import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SolicitudesAdminComponent } from './solicitudes-admin/solicitudes-admin.component';
import { HistorialSolicitudesComponent } from './historial-solicitudes/historial-solicitudes.component';

const routes: Routes = [
  { path: 'solicitudes', component: SolicitudesAdminComponent },
  { path: 'historial', component: HistorialSolicitudesComponent },
  { path: '', redirectTo: 'solicitudes', pathMatch: 'full' },
];

@NgModule({ imports: [RouterModule.forChild(routes)], exports: [RouterModule] })
export class DashboardAdminRoutingModule {}
