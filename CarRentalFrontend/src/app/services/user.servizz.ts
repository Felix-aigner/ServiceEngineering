import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserModel} from '../models/user.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {ConfigService} from './config.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  isLoggedIn = new BehaviorSubject<boolean>(false);
  currUser = new BehaviorSubject<UserModel>(new UserModel());

  private readonly commonHttpHeaders;

  constructor(private http: HttpClient, private configService: ConfigService) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');
  }

  accountURL() {
    if(environment.production) {
      return this.configService.config.restUrl + 'accounts';
    } else {
      return 'http://localhost:5000/accounts';
    }
  }

  login(username: string, password: string): Observable<UserModel> {
    return this.http.post<UserModel>(this.accountURL() + '/login', {
      id: "",
      username: username,
      password: password,
      firstname: "",
      lastname: "",
      isAdministrator: ""
    }, {
      headers: this.commonHttpHeaders
    });
  }

  // tslint:disable-next-line:typedef
  register(firstname: string, lastname: string, username: string, password: string) {
    return this.http.post(this.accountURL(), {
      firstname,
      lastname,
      username,
      password
    }, {
      headers: this.commonHttpHeaders
    });
  }
}
