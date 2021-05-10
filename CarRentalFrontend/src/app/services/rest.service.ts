import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {UserService} from "./user.servizz";
import {ConfigService} from './config.service';
import {catchError, map} from "rxjs/operators";
import {CurrencyEnum} from "../models/car.model";

@Injectable({
  providedIn: 'root'
})
export class RestService {

  private readonly commonHttpHeaders;

  constructor(protected http: HttpClient, private configService: ConfigService, private userService: UserService) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');
  }

  carURL() {
    return (environment.production) ? this.configService.config.restUrl + 'cars' : 'http://localhost:5000/cars';
  }

  getAllCars(currency: CurrencyEnum): Observable<any> {
    return this.http.get(this.carURL() + "?cy=" + CurrencyEnum[currency],
      {
        headers: this.commonHttpHeaders
          .append('token', this.userService.currUser.value.token)
      }
    ).pipe(
      catchError(err => {
        throw Error('Load Vote Failed!' + err.message);
      })
    );
  }

  getAllRentals(): Observable<any> {
    return this.http.get(environment.production ? (this.configService.config.restUrl+'rentals') : 'http://localhost:5000/rentals',
      {
        headers: this.commonHttpHeaders
          .append('token', this.userService.currUser.value.token)
      }
    ).pipe(
      catchError(err => {
        throw Error('Load Vote Failed!' + err.message);
      })
    );
  }
}
