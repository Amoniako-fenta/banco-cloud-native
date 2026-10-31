import { Routes } from '@angular/router';

import { AuthGuard } from './core/guards/auth.guard';
import { InicioComponent } from './features/inicio/inicio.component';
import { SucursalesListComponent } from './features/sucursales/sucursales-list/sucursales-list.component';
import { CajerosListComponent } from './features/cajeros/cajeros-list/cajeros-list.component';
import { MiPerfilComponent } from './features/perfil/mi-perfil/mi-perfil.component';
import { UsuariosPendientesComponent } from './features/admin/usuarios-pendientes/usuarios-pendientes.component';

export const routes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'sucursales', component: SucursalesListComponent, canActivate: [AuthGuard] },
  { path: 'cajeros', component: CajerosListComponent, canActivate: [AuthGuard] },
  { path: 'mi-perfil', component: MiPerfilComponent, canActivate: [AuthGuard] },
  {
    path: 'admin/usuarios',
    component: UsuariosPendientesComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  { path: '**', redirectTo: '' }
];
