import { Injectable } from '@angular/core';
import { Abrigo } from '../abrigo/abrigo';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AbrigosService {
  protected abrigoList: Abrigo[] = [];

  url = 'http://localhost:8080/abrigos/'

  getAbrigoById(id: String): Observable<Abrigo> {
    return this.http.get<Abrigo>(this.url + id);
  }

  constructor(private http: HttpClient) { }
}
