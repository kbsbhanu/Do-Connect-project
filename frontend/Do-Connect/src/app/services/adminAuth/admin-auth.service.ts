import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';

const BASIC_URL = environment['BASIC_URL'];
export const AUTH_HEADER = 'authorization';

@Injectable({
  providedIn: 'root',
})
export class AdminAuthService {
  constructor(private http: HttpClient) {}

  sigin(data): Observable<any> {
    return this.http.post(BASIC_URL + 'api/admin/login', data);
  }

  registerAdmin(data): Observable<any> {
    return this.http.post(BASIC_URL + 'api/admin/sign-up', data);
  }

  handleError<T>(operation = 'operation', result?: T): any {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }
}
