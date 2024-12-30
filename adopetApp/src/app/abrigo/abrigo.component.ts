import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AbrigosService } from '../abrigo/abrigos.service';
import { Abrigo } from '../abrigo/abrigo';
import { CopyTextDirective } from './copy-text.directive';

@Component({
  selector: 'app-abrigo',
  imports: [CopyTextDirective],
  templateUrl: './abrigo.component.html'
})
export class AbrigoComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  abrigoService: AbrigosService = inject(AbrigosService);
  abrigo!: Abrigo;
  tutorId: any;

  constructor() {
    var abrigoId = this.route.snapshot.params['id'];
    this.tutorId = localStorage.getItem("id");

    this.abrigoService.getAbrigoById(abrigoId)
    .subscribe(abrigo => this.abrigo = abrigo);
  }
}
