import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Car, CurrencyEnum} from "../car/models/car.model";
import {UserService} from "./user.service";
import {catchError, map} from "rxjs/operators";

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

  getAllCars(currency: CurrencyEnum = CurrencyEnum.USD): Observable<any> {
    return this.http.get( this.carURL + "?cy=" + "USD",
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
