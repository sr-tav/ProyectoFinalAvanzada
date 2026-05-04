import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DashboardAdminRoutingModule } from './dashboard-admin-routing.module';
import { SolicitudesAdminComponent } from './solicitudes-admin/solicitudes-admin.component';
import { HistorialSolicitudesComponent } from './historial-solicitudes/historial-solicitudes.component';
import { SharedModule } from '../../shared/shared.module';
// reutiliza el componente de responder del módulo funcionario — pero copiamos el inline para independencia
import { DashboardFuncionarioModule } from '../dashboard-funcionario/dashboard-funcionario.module';

@NgModule({
  declarations: [SolicitudesAdminComponent, HistorialSolicitudesComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, DashboardAdminRoutingModule, SharedModule, DashboardFuncionarioModule],
})
export class DashboardAdminModule {}
