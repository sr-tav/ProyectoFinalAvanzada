import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SolicitudesFuncionarioComponent } from './solicitudes-funcionario/solicitudes-funcionario.component';

const routes: Routes = [
  { path: 'solicitudes', component: SolicitudesFuncionarioComponent },
  { path: '', redirectTo: 'solicitudes', pathMatch: 'full' },
];

@NgModule({ imports: [RouterModule.forChild(routes)], exports: [RouterModule] })
export class DashboardFuncionarioRoutingModule {}
