import { Injectable } from '@angular/core';
import { PetsPage } from './pet-page';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PetsService {
  url = 'http://localhost:8080/pets/adopt'

  getAllPets(): Observable<PetsPage> {
    return this.http.get<PetsPage>(this.url);
  }

  constructor(private http: HttpClient, private router: Router) { }
}
