import { Component, inject } from '@angular/core';
import { PetsService } from './pets.service';
import { PetsPage } from './pet-page';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-pet',
  imports: [NgFor, RouterLink, NgIf],
  templateUrl: './pet.component.html'
})
export class PetComponent {
  petsService: PetsService = inject(PetsService);
  petsPage!: PetsPage;

  constructor(
  ) {
    var id = localStorage.getItem("id");
    this.petsService.getAllPetsFromAbrigoId(id)
    .subscribe(petsPage => this.petsPage = petsPage);
  }
}
