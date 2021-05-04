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
  accountURL: string;


  private readonly commonHttpHeaders;

  constructor(private http: HttpClient) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');
    if(environment.production) {
      this.getURL();
    } else {
      this.accountURL = 'http://localhost:5000/accounts';
    }
  }

  getURL() {
    this.http.get("http://api.ipify.org/?format=json")
      .subscribe((res:any)=> {
      this.accountURL = 'http://' + res.ip + ':5000/accounts';
    });
  }

  login(username: string, password: string): Observable<UserModel> {
    return this.http.post<UserModel>(this.accountURL + '/login', {
      username, password
    }, {
      headers: this.commonHttpHeaders
    });
  }

  // tslint:disable-next-line:typedef
  register(firstname: string, lastname: string, username: string, password: string) {
    return this.http.post(this.accountURL, {
      firstname,
      lastname,
      username,
      password
    }, {
      headers: this.commonHttpHeaders
    });
  }
}
