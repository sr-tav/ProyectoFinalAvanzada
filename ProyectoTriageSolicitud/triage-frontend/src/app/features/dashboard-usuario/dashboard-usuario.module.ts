import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { DashboardUsuarioRoutingModule } from './dashboard-usuario-routing.module';
import { CrearSolicitudComponent } from './crear-solicitud/crear-solicitud.component';
import { MisSolicitudesComponent } from './mis-solicitudes/mis-solicitudes.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [CrearSolicitudComponent, MisSolicitudesComponent],
  imports: [CommonModule, ReactiveFormsModule, DashboardUsuarioRoutingModule, SharedModule],
})
export class DashboardUsuarioModule {}
