import { Injectable } from '@angular/core';
import { PetsPage } from './pet-page';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Pet } from './pet';
import { Adocao } from './adocao-pet/adocao';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PetsService {
  private apiUrl = environment.API_URL

  getAllPets(): Observable<PetsPage> {
    return this.http.get<PetsPage>(this.apiUrl + "/pets/adopt");
  }

  getAllPetsFromAbrigoId(id: string | null): Observable<PetsPage> {
    return this.http.get<PetsPage>(this.apiUrl + "/pets/abrigo/" + id);
  }

  cadastrarPet(pet: Pet | undefined) {
    this.http.post<Pet>(this.apiUrl, pet)
    .subscribe({
      next: (v) => {
        console.log(v)
        this.router.navigate(["/pets"])
      },
      error: (e: HttpErrorResponse) => {
        console.log(e)
        alert("Erro ao cadastrar, verifique formulário. " + e.status)
        this.router.navigate(["/pets/cadastro-pet"])
      }
    });
  }

  constructor(private http: HttpClient, private router: Router) { }
}
