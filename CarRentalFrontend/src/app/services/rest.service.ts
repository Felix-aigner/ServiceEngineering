import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {UserService} from "./user.service";
import {catchError, map} from "rxjs/operators";
import {CurrencyEnum} from "../models/car.model";

@Injectable({
  providedIn: 'root'
})
export class RestService {

  carURL: string;
  rentalURL: string;
  private readonly commonHttpHeaders;

  constructor(protected http: HttpClient, private userService: UserService) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');

    if(environment.production) {
      this.getURL();
    } else {
      this.carURL = 'http://localhost:5000/cars';
      this.rentalURL = 'http://localhost:5000/rentals';
    }
  }

  getURL() {
    this.http.get("http://api.ipify.org/?format=json")
      .subscribe((res:any)=> {
        this.carURL = 'http://' + res.ip + ':5000/cars';
        this.rentalURL = 'http://' + res.ip + ':5000/rentals';
      });
  }

  getAllCars(currency: CurrencyEnum): Observable<any> {
    return this.http.get( this.carURL + "?cy=" + CurrencyEnum[currency],
      {
        headers: this.commonHttpHeaders
      }
    ).pipe(
      catchError(err => {
        throw Error('Load Vote Failed!' + err.message);
      })
    );
  }

  getAllRentals(): Observable<any> {
    return this.http.get( this.rentalURL,
      {
        headers: this.commonHttpHeaders
      }
    ).pipe(
      catchError(err => {
        throw Error('Load Vote Failed!' + err.message);
      })
    );
  }
}
