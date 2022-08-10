import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserStorageService } from '../storage/user-storage.service';

const BASIC_URL = environment['BASIC_URL'];
export const AUTH_HEADER = 'authorization';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user: any;

  giveUserData(): any {
    return this.user;
  }

  constructor(
    private http: HttpClient,
    private userStorageService: UserStorageService
  ) {}

  login(username: string, password: string): any {
    return this.http
      .post<[]>(
        BASIC_URL + 'authenticate',
        {
          username,
          password,
        },
        { observe: 'response' }
      )
      .pipe(
        map((res: HttpResponse<any>) => {
          this.userStorageService.saveUser(res.body);
          this.user = username;
          const tokenLength = res.headers.get(AUTH_HEADER).length;
          const bearerToken = res.headers
            .get(AUTH_HEADER)
            .substring(7, tokenLength);
          this.userStorageService.saveToken(bearerToken);
          return res;
        })
      );
  }

  register(data): Observable<any> {
    return this.http.post(BASIC_URL + 'sign-up', data);
  }

  handleError<T>(operation = 'operation', result?: T): any {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }
}
