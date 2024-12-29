import { Injectable } from '@angular/core';
import { Tutor } from './tutor';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  url = 'http://localhost:8080/tutores'

  async postNewTutor(tutor: Tutor) {
    this.http.post(this.url, tutor).subscribe(newTutor => {
      console.log('New tutor: ', newTutor);
    })
  }

  constructor(private http: HttpClient) { }
}
