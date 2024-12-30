import { Component, inject } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { Adocao } from './adocao';
import { NgIf } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AdocaoService } from './adocao.service';

@Component({
  selector: 'app-adocao-pet',
  imports: [ FormsModule, NgIf],
  templateUrl: './adocao-pet.component.html'
})
export class AdocaoPetComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  adocaoService: AdocaoService = inject(AdocaoService);

  adocao = new Adocao("", "");
  adoptedPet: string;

  cadastrar() {
    this.adocao.pet = this.adoptedPet;
    console.log(this.adocao)
    this.adocaoService.adotarPet(this.adocao);
  }

  constructor () {
    this.adoptedPet = this.route.snapshot.params['id'];
  }
}
