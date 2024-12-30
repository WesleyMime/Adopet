import { Injectable } from '@angular/core';
import { Tutor } from './register-tutor/tutor';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Abrigo } from './register-abrigo/abrigo';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  url = 'http://localhost:8080'

  postNewTutor(tutor: Tutor) {
    this.http.post(this.url + "/tutores", tutor)
    .subscribe({
      next: (v) => {
        console.log(v)
        this.router.navigate(["/"])
      },
      error: (e: HttpErrorResponse) => {
        console.log(e)
        alert("Erro ao cadastrar, verifique formulário. " + e.status)
        this.router.navigate(["/cadastro"])
      }
    });
  }

  async postNewAbrigo(abrigo: Abrigo) {
    this.http.post(this.url + "/abrigos", abrigo)
    .subscribe({
      next: (v) => {
        console.log(v)
        this.router.navigate(["/"])
      },
      error: (e: HttpErrorResponse) => {
        console.log(e)
        alert("Erro ao cadastrar, verifique formulário. " + e.status)
        this.router.navigate(["/cadastro-abrigo"])
      }
    });
  }

  constructor(private http: HttpClient, private router: Router) { }
}
