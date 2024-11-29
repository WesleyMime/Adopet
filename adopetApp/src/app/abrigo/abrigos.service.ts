import { Injectable } from '@angular/core';
import { Abrigo } from '../abrigo/abrigo';

@Injectable({
  providedIn: 'root'
})
export class AbrigosService {
  protected abrigoList: Abrigo[] = [];

  url = 'http://localhost:8080/abrigos/'

  async getAbrigoById(id: String): Promise<Abrigo> {
    const data = (await fetch(this.url + id));
    return await data.json() ?? {};
  }

  constructor() { }
}
