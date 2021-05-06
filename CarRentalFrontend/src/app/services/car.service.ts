import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';

import {Car, CurrencyEnum} from '../car/models/car.model';
import {UserService} from './user.service';
import {MatDialog} from '@angular/material/dialog';
import {ErrorDialogComponent} from '../dialogs/error-dialog/error-dialog.component';
import {IRental} from '../models/rental.model';
import {catchError, map} from 'rxjs/operators';
import {environment} from "../../environments/environment";


type EntityResponseType = HttpResponse<Car>;

@Injectable({providedIn: 'root'})
export class CarService {
  carURL: string;
  rentalURL: string;

  private readonly commonHttpHeaders;
  selectedCurrency = new BehaviorSubject<CurrencyEnum>(CurrencyEnum.EUR);

  public loadedCars = new BehaviorSubject<Car[]>([]);

  public myRentals = new BehaviorSubject<IRental[]>([]);
  public allRentals = new BehaviorSubject<IRental[]>([]);

  constructor(
    protected http: HttpClient,
    private userService: UserService,
    private dialog: MatDialog
  ) {
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

  update(car: Car): Observable<EntityResponseType> {
    console.log(car);
    return this.http.put<Car>(this.carURL, {
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

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<Car>(`${this.carURL}/${id}`, {observe: 'response'});
  }

  getCarsFromStore(): void {

  }

  //this.loadedCars.next(response.body);

  delete(id: number): any {
    return this.http.delete(`${this.carURL}/${id}`, {
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
  }

  bookCar(rental: IRental): void {
    console.log(rental);
    this.http.post<IRental>(this.rentalURL, rental, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).subscribe(() => {
        this.queryMyRentals();
        if (this.userService.currUser.value.isAdministrator) {
          this.queryAllRentals();
        }
      }
    );
  }

  releaseCar(rental: IRental): void {
    this.http.put<IRental>(`${this.rentalURL}/${rental.id}`, {}, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).subscribe(() => {
        this.queryMyRentals();
        if (this.userService.currUser.value.isAdministrator) {
          this.queryAllRentals();
        }
      }
    );
  }

  getCar(carId: number): Observable<Car> {
    return this.http.get<Car>(`${this.carURL}/${carId}`, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      catchError(error => {
        return of(null);
      }),
      map((value) => value.body)
    );
  }

  queryMyRentals(): void {
    this.http.get<IRental[]>(this.rentalURL + '/my-rentals', {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      catchError(error => {
        return of(null);
      }),
      map((value: HttpResponse<IRental[]>) => value.body),
    ).subscribe((value) => {
      this.myRentals.next(value.filter(rental => rental.car != null));
    });
  }

  queryAllRentals(): void {
    this.http.get<IRental[]>(this.rentalURL, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).pipe(
      catchError(error => {
        return of(null);
      }),
      map((value: HttpResponse<IRental[]>) => value.body),
    ).subscribe((value) => {
      this.allRentals.next(value.filter(rental => rental.car != null));
    });
  }
}
