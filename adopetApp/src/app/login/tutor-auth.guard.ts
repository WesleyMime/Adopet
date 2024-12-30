import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router } from '@angular/router';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class TutorAuthGuard implements CanActivate{

  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(): boolean {
    if (this.loginService.isLoggedIn() && this.loginService.isTutor()) {
      return true;
    } else {
      alert("Unauthorized")
      this.router.navigate(['/']);
      return false;
    }
  }
}