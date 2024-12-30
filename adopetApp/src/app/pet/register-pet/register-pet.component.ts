import { Component, inject } from '@angular/core';
import { PetsService } from '../pets.service';
import { Pet } from '../pet';
import { FormsModule, NgModel } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register-pet',
  imports: [FormsModule, NgIf],
  templateUrl: './register-pet.component.html'
})
export class RegisterPetComponent {
  petsService: PetsService = inject(PetsService);

  pet = new Pet("","","","","",false,"","","");

  register() {
    this.pet.abrigo = localStorage.getItem("id");
    this.petsService.cadastrarPet(this.pet);
  }

}
