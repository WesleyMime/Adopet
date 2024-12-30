import { Component, inject } from '@angular/core';
import { PetsService } from './pets.service';
import { PetsPage } from './pet-page';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-pet',
  imports: [NgFor],
  templateUrl: './pet.component.html'
})
export class PetComponent {
  petsService: PetsService = inject(PetsService);
  petsPage!: PetsPage;

  constructor(
  ) {
    var email = localStorage.getItem("email");
    this.petsService.getAllPetsFromAbrigo(email)
    .subscribe(petsPage => this.petsPage = petsPage);
  }
}
