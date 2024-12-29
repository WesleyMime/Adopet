import { Injectable } from '@angular/core';
import { LoginForm } from './LoginForm';
import { HttpClient, HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  url = "http://localhost:8080/auth/login"

  login(login: LoginForm) {
    this.http.post(this.url, login)
    .subscribe({
      next: (v) => {
        this.setSession(v)
        this.router.navigate(["/home"])
      },
      error: (e: HttpErrorResponse) => {
        alert("Erro ao entrar, verifique formulário. " + e.status)
        this.router.navigate(["/login"])
      }
    });
  }

  setSession(authResponse: any) {
    localStorage.setItem("token", authResponse.token);
    localStorage.setItem("expiresAt", authResponse.expiresAt);

  }

  public isLoggedIn(): boolean {
    var expiration = localStorage.getItem("expiresAt");
    if (expiration == null) return false;
    return Date.now() < Number.parseInt(expiration);
    if (Date.now() > Number.parseInt(expiration)) {
      this.logout;
      return false;
    }
    return true;
  }

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("expiresAt");
  }

  constructor(private http: HttpClient, private router: Router) { }

}