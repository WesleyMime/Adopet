import { Injectable } from '@angular/core';
import { PetsPage } from './pet-page';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { Pet } from './pet';
import { Adocao } from './adocao-pet/adocao';

@Injectable({
  providedIn: 'root'
})
export class PetsService {
  url = 'http://localhost:8080/pets'

  getAllPets(): Observable<PetsPage> {
    return this.http.get<PetsPage>(this.url + "/adopt");
  }

  getAllPetsFromAbrigoId(id: string | null): Observable<PetsPage> {
    return this.http.get<PetsPage>(this.url + "/abrigo/" + id);
  }

  cadastrarPet(pet: Pet | undefined) {
    this.http.post<Pet>(this.url, pet)
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
