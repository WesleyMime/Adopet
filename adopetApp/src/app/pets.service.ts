import { Injectable } from '@angular/core';
import { PetsPage } from './pet-page';

@Injectable({
  providedIn: 'root'
})
export class PetsService {
  url = 'http://localhost:8080/pets/adopt'

  async getAllPets(): Promise<PetsPage> {
    const data = (await fetch(this.url));
    return await data.json() ?? {};
  }

  constructor() { }
}
