import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Adocao } from './adocao';

@Injectable({
  providedIn: 'root'
})
export class AdocaoService {
  url = 'http://localhost:8080/adocao'

  adotarPet(adocao: Adocao | undefined) {
    this.http.post<Adocao>(this.url, adocao)
    .subscribe({
      next: (v) => {
        console.log(v)
        this.router.navigate(["/pets"])
      },
      error: (e: HttpErrorResponse) => {
        console.log(e)
        alert("Erro ao cadastrar, verifique formulário. " + e.status)
      }
    });
  }

  constructor(private http: HttpClient, private router: Router) { }
}
