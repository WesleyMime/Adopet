import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AbrigoComponent } from './abrigo/abrigo.component';
import { InitialComponent } from './initial/initial.component';
import { RegisterComponent } from './register-tutor/register.component';
import { LoginComponent } from './login/login.component';

export const routes: Routes = [
    {
        path: '',
        component: InitialComponent,
    },
    {
        path: 'home',
        component: HomeComponent,
        title: 'Adopet'
    },
    {
        path: 'pet/:id',
        component: AbrigoComponent,
        title: 'Adopet'
    },
    {
        path: 'cadastro',
        component: RegisterComponent,
        title: 'Adopet'
    },
    {
        path: 'login',
        component: LoginComponent,
        title: 'Adopet'
    }
];
