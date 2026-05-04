import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DashboardFuncionarioRoutingModule } from './dashboard-funcionario-routing.module';
import { SolicitudesFuncionarioComponent } from './solicitudes-funcionario/solicitudes-funcionario.component';
import { ResponderSolicitudComponent } from './responder-solicitud/responder-solicitud.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [SolicitudesFuncionarioComponent, ResponderSolicitudComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, DashboardFuncionarioRoutingModule, SharedModule],
  exports: [ResponderSolicitudComponent],
})
export class DashboardFuncionarioModule {}
