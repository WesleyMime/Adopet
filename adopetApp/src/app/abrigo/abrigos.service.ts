import { Injectable } from '@angular/core';
import { Abrigo } from '../abrigo/abrigo';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AbrigosService {
  private apiUrl = environment.API_URL
  protected abrigoList: Abrigo[] = [];

  getAbrigoById(id: String): Observable<Abrigo> {
    return this.http.get<Abrigo>(this.apiUrl + '/abrigos/' + id);
  }

  constructor(private http: HttpClient) { }
}
