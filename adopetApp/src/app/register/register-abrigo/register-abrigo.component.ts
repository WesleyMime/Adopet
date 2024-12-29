import { Component, inject } from '@angular/core';
import { RegisterService } from '../register.service';
import { Abrigo } from './abrigo';
import { FormsModule, NgModel } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-abrigo',
  imports: [FormsModule, NgIf],
  templateUrl: './register-abrigo.component.html'
})
export class RegisterAbrigoComponent {
  registerService: RegisterService = inject(RegisterService);
  abrigo = new Abrigo("", "", "", "", "", "");

  cadastrar() {
    console.log(this.abrigo);
    this.registerService.postNewAbrigo(this.abrigo);
  }

  constructor(private router: Router) {
  }
}