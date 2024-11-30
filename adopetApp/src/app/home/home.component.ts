import { Component, inject, Inject } from '@angular/core';
import { PetsService } from './pets.service';
import { NgFor } from '@angular/common';
import { PetsPage } from './pet-page';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [NgFor, RouterModule],
  templateUrl: './home.component.html'
})
export class HomeComponent {
  petsService: PetsService = inject(PetsService);
  petsPage!: PetsPage;

  constructor() {
    this.petsService.getAllPets().then((petsPage: PetsPage) => {
      this.petsPage = petsPage;
    });
  }
}
