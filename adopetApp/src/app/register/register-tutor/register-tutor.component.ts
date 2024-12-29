import { Component, inject } from '@angular/core';
import { RegisterService } from '../register.service';
import { FormsModule } from '@angular/forms';
import { Tutor } from './tutor';
import { NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, NgIf, RouterLink],
  templateUrl: './register-tutor.component.html'
})
export class RegisterTutorComponent {
  registerService: RegisterService = inject(RegisterService);
  tutor = new Tutor("", "", "", "");

  cadastrar() {
    console.log(this.tutor);
    this.registerService.postNewTutor(this.tutor);
  }

  constructor() {}
}
