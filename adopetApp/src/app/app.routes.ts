import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AbrigoComponent } from './abrigo/abrigo.component';
import { InitialComponent } from './initial/initial.component';
import { RegisterTutorComponent } from './auth/register/register-tutor/register-tutor.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterAbrigoComponent } from './auth/register/register-abrigo/register-abrigo.component';
import { AbrigoAuthGuard } from './auth/abrigo-auth.guard';
import { TutorAuthGuard } from './auth/tutor-auth.guard';
import { PetComponent } from './pet/pet.component';

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
        path: 'pets',
        component: PetComponent,
        title: 'Adopet',
        canActivate: [AbrigoAuthGuard]
    },
    {
        path: 'pet/:id',
        component: AbrigoComponent,
        title: 'Adopet',
        canActivate: [TutorAuthGuard]
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
