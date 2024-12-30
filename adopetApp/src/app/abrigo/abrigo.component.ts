import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AbrigosService } from '../abrigo/abrigos.service';
import { Abrigo } from '../abrigo/abrigo';

@Component({
  selector: 'app-abrigo',
  imports: [],
  templateUrl: './abrigo.component.html'
})
export class AbrigoComponent {
  route: ActivatedRoute = inject(ActivatedRoute);
  abrigoService: AbrigosService = inject(AbrigosService);
  abrigoId = '';
  abrigo!: Abrigo; 

  constructor() {
    this.abrigoId = this.route.snapshot.params['id'];

    this.abrigoService.getAbrigoById(this.abrigoId)
    .subscribe(abrigo => this.abrigo = abrigo);
  }
}
