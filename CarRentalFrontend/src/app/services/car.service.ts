import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';

// @ts-ignore
import {Car, CurrencyEnum} from '../models/car.model';
import {UserService} from './user.service';
import {MatDialog} from '@angular/material/dialog';
import {Rental} from '../models/rental.model';
import {catchError, map} from 'rxjs/operators';
import {environment} from "../../environments/environment";
import {CarSelectorService} from "../car/car-selector.service";
import {State} from "../app.store";
import {Store} from "@ngrx/store";
import * as restAction from '../car/+state/rest.actions';


type EntityResponseType = HttpResponse<Car>;

@Injectable({providedIn: 'root'})
export class CarService {
  carURL: string;
  rentalURL: string;

  private readonly commonHttpHeaders;
  selectedCurrency = new BehaviorSubject<CurrencyEnum>(CurrencyEnum.EUR);

  constructor(
    protected http: HttpClient,
    private userService: UserService,
    private dialog: MatDialog,
    private carSelector: CarSelectorService,
    private store: Store<State>
  ) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token'])
      .set('Access-Control-Allow-Origin', '*');

    if (environment.production) {
      this.getURL();
    } else {
      this.carURL = 'http://localhost:5000/cars';
      this.rentalURL = 'http://localhost:5000/rentals';
    }
  }

  getURL() {
    this.http.get("http://api.ipify.org/?format=json")
      .subscribe((res: any) => {
        this.carURL = 'http://' + res.ip + ':5000/cars';
        this.rentalURL = 'http://' + res.ip + ':5000/rentals';
      });
  }

  createNewCar(car: Car): Observable<any>  {
    return this.http.post<Car>(this.carURL, {
      car: car,
      method: "post"
    }, {
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      map((_ => {
        this.store.dispatch(restAction.GetAllRentals());
        this.store.dispatch(restAction.GetAllCars({currency: this.selectedCurrency.getValue()}))
      })));
  }

  updateCar(car: Car): Observable<any> {
    return this.http.put<string>(this.carURL, {
      car: car,
      method: "put"
    }, {
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      map((_ => {
        this.store.dispatch(restAction.GetAllRentals());
        this.store.dispatch(restAction.GetAllCars({currency: this.selectedCurrency.getValue()}))
      })));
  }

  delete(id: string): void {
    this.http.post(this.carURL + "/delete", {
      id: id,
      method: "delete"
    }, {
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      map((_ => {
        this.store.dispatch(restAction.DeleteCarByIdSuccess({id}))
      }))).subscribe();
  }

  bookCar(rental: Rental): void {
    console.log(rental);
    this.http.post<Rental>(this.rentalURL + "/create",
      {
        rental: rental,
        method: "create"
      },
      {
        observe: 'response',
        headers: this.commonHttpHeaders
          .append('token', this.userService.currUser.value.token)
      }).pipe(
      map((_) => {
        this.store.dispatch(restAction.GetAllRentals());
        this.store.dispatch(restAction.GetAllCars({currency: this.selectedCurrency.getValue()}));
      })).subscribe()
  }

  releaseCar(rental: Rental): void {
    this.http.post<Rental>(this.rentalURL + "/return",
      {
        rental: rental,
        method: "return"
      },
      {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      map((_) => {
        this.store.dispatch(restAction.GetAllRentals());
        this.store.dispatch(restAction.GetAllCars({currency: this.selectedCurrency.getValue()}));
      })).subscribe()
  }
}
