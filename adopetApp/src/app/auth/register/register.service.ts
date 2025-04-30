import { Injectable } from '@angular/core';
import { Tutor } from './register-tutor/tutor';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Abrigo } from './register-abrigo/abrigo';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private apiUrl = environment.API_URL

  postNewTutor(tutor: Tutor) {
    this.http.post(this.apiUrl + "/tutores", tutor)
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
    this.http.post(this.apiUrl + "/abrigos", abrigo)
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
