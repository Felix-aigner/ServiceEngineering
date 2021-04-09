import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserModel} from '../models/user.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  isLoggedIn = new BehaviorSubject<boolean>(false);
  currUser = new BehaviorSubject<UserModel>(new UserModel());
  backendUrl = 'http://localhost:5000/account';

  private readonly commonHttpHeaders;

  constructor(private http: HttpClient) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json');
  }

  login(username: string, password: string): Observable<UserModel> {
    return this.http.post<UserModel>(this.backendUrl + '/login', {
      username, password
    });
  }

  // tslint:disable-next-line:typedef
  register(firstname: string, lastname: string, username: string, password: string) {
    return this.http.post(this.backendUrl, {
      firstname,
      lastname,
      username,
      password
    });
  }
}
