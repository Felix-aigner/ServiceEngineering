import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserModel} from '../models/user.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  isLoggedIn = new BehaviorSubject<boolean>(false);
  currUser = new BehaviorSubject<UserModel>(new UserModel());
  accoutURL = environment.webserviceUrl + 'accounts';

  private readonly commonHttpHeaders;

  constructor(private http: HttpClient) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');
  }

  login(username: string, password: string): Observable<UserModel> {
    return this.http.post<UserModel>(this.accoutURL + '/login', {
      username, password
    }, {
      headers: this.commonHttpHeaders
    });
  }

  // tslint:disable-next-line:typedef
  register(firstname: string, lastname: string, username: string, password: string) {
    return this.http.post(this.accoutURL, {
      firstname,
      lastname,
      username,
      password
    }, {
      headers: this.commonHttpHeaders
    });
  }
}
