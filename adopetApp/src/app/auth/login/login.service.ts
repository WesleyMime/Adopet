import { Injectable } from '@angular/core';
import { LoginForm } from './LoginForm';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
        if (this.isAbrigo()) {
            this.router.navigate(["/pets"])
        } else {
          this.router.navigate(["/home"])
        }
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
    localStorage.setItem("role", authResponse.role);
    localStorage.setItem("id", authResponse.id);
  }

  public isLoggedIn(): boolean {
    var expiration = localStorage.getItem("expiresAt");
    if (expiration == null) return false;
    if (Date.now() > Number.parseInt(expiration)) {
      this.logout;
      return false;
    }
    return true;
  }

  public isAbrigo(): boolean {
    var role = localStorage.getItem("role");
    if (role == "[ABRIGO]") return true;
    return false;
  }

  public isTutor(): boolean {
    var role = localStorage.getItem("role");
    if (role == "[TUTOR]") return true;
    return false;
  }

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("expiresAt");
    localStorage.removeItem("role");
    localStorage.removeItem("id");
  }

  constructor(private http: HttpClient, private router: Router) { }

}