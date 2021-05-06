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

  public loadedCars = new BehaviorSubject<Car[]>([]);

  public myRentals = new BehaviorSubject<Rental[]>([]);
  public allRentals = new BehaviorSubject<Rental[]>([]);

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

  save(car: Car): Observable<EntityResponseType> {
    console.log(car);
    return this.http.post<Car>(this.carURL, {
      id: car.id,
      type: car.type,
      brand: car.brand,
      kwPower: car.kwPower,
      usdPrice: car.price
    }, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
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
        debugger
        this.store.dispatch(restAction.GetAllCars({currency: this.selectedCurrency.getValue()}))
      }))
    );
  }

  getCarsFromStore(): void {
    this.carSelector.getAllCarsFromStore().pipe(
    ).subscribe((cars: Car[]) => {
      this.loadedCars.next(cars);
    })
  }

  delete(id: number): any {
    return this.http.delete(`${this.carURL}/${id}`, {
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
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
      })).subscribe()
  }

  releaseCar(rental: Rental): void {
    this.http.put<Rental>(`${this.rentalURL}/${rental.id}`, {}, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      map((_) => {
        this.store.dispatch(restAction.GetAllRentals());
      })).subscribe()
  }
}
