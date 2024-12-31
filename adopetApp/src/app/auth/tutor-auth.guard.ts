import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router } from '@angular/router';
import { LoginService } from './login/login.service';

@Injectable({
  providedIn: 'root'
})
export class TutorAuthGuard implements CanActivate{

  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(): boolean {
    if (this.loginService.isLoggedIn() && this.loginService.isTutor()) {
      return true;
    } else {
      alert("Faça login para acessar.")
      this.router.navigate(['/login']);
      return false;
    }
  }
}