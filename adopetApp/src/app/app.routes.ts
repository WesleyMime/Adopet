import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AbrigoComponent } from './abrigo/abrigo.component';
import { InitialComponent } from './initial/initial.component';
import { RegisterTutorComponent } from './register/register-tutor/register-tutor.component';
import { LoginComponent } from './login/login.component';
import { RegisterAbrigoComponent } from './register/register-abrigo/register-abrigo.component';

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
        component: RegisterTutorComponent,
        title: 'Adopet'
    },
    {
        path: 'cadastro-abrigo',
        component: RegisterAbrigoComponent,
        title: 'Adopet'
    },
    {
        path: 'login',
        component: LoginComponent,
        title: 'Adopet'
    }
];
