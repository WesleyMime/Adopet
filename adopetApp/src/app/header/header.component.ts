import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [ RouterLink, NgIf],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  role = localStorage.getItem("role");
}
