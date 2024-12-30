import { Component, inject } from '@angular/core';
import { LoginService } from './login.service';
import { LoginForm } from './LoginForm';
import { FormsModule, NgForm } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  loginService: LoginService = inject(LoginService);
  loginForm = new LoginForm("", "");

  login() {
    this.loginService.login(this.loginForm);
  }
}
