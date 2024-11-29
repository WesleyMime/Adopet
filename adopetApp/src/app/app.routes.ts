import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AbrigoComponent } from './abrigo/abrigo.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        title: 'Home Page'
    },
    {
        path: 'pet/:id',
        component: AbrigoComponent,
        title: 'Abrigo'
    }
];
