import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FooterImgComponent } from '../footer-img/footer-img.component';

@Component({
  selector: 'app-initial',
  imports: [RouterLink, FooterImgComponent],
  templateUrl: './initial.component.html',
  styleUrl: './initial.component.css'
})
export class InitialComponent {

}
