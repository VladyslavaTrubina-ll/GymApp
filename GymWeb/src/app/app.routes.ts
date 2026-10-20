import { Routes } from '@angular/router';
import { VistaGeneral } from './components/vista-general/vista-general';
import { VistaDetalle } from './components/vista-general/vista-detalle/vista-detalle';

export const routes: Routes = [
  { path: '', component: VistaGeneral },
  { path: 'rutina/:id', component: VistaDetalle }
];