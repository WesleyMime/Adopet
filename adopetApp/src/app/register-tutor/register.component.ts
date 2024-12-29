import { Component, inject } from '@angular/core';
import { RegisterService } from './register.service';
import { FormsModule } from '@angular/forms';
import { Tutor } from './tutor';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule, NgIf],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  registerService: RegisterService = inject(RegisterService);
  tutor = new Tutor("", "", "", "");

  cadastrar() {
    console.log(this.tutor);
    this.registerService.postNewTutor(this.tutor);
  }

  constructor() {
  }

}
