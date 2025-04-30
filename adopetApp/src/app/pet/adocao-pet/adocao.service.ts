import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Adocao } from './adocao';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdocaoService {
  private apiUrl = environment.API_URL

  adotarPet(adocao: Adocao | undefined) {
    this.http.post<Adocao>(this.apiUrl + "/adocao", adocao)
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
