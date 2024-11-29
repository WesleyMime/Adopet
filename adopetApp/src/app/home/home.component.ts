import { Component, inject, Inject } from '@angular/core';
import { PetsService } from '../pets.service';
import { NgFor } from '@angular/common';
import { PetsPage } from '../pet-page';

@Component({
  selector: 'app-home',
  imports: [NgFor],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
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
