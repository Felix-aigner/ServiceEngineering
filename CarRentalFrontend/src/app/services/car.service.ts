import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';

import {SERVER_API_URL} from '../app.constants';
import {CurrencyEnum, ICar} from '../models/car.model';
import {UserService} from './user.service';
import {MatDialog} from '@angular/material/dialog';
import {ErrorDialogComponent} from '../dialogs/error-dialog/error-dialog.component';
import {IRental} from '../models/rental.model';
import {catchError, map} from 'rxjs/operators';


type EntityResponseType = HttpResponse<ICar>;

@Injectable({providedIn: 'root'})
export class CarService {
  public carURL = SERVER_API_URL + 'cars';
  public rentalURL = SERVER_API_URL + 'rentals';

  private readonly commonHttpHeaders;
  selectedCurrency = new BehaviorSubject<CurrencyEnum>(CurrencyEnum.EUR);

  public loadedCars = new BehaviorSubject<ICar[]>([]);

  public myRentals = new BehaviorSubject<IRental[]>([]);
  public allRentals = new BehaviorSubject<IRental[]>([]);

  constructor(protected http: HttpClient, private userService: UserService, private dialog: MatDialog) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json')
      .set('Access-Control-Allow-Methods', ['POST', 'GET', 'DELETE', 'OPTIONS', 'PUT'])
      .set('Access-Control-Allow-Headers', ['token', 'Content-Type', 'Accept'])
      .set('Access-Control-Allow-Origin', '*');
  }

  save(car: ICar): Observable<EntityResponseType> {
    console.log(car);
    return this.http.post<ICar>(this.carURL, {
      id: car.id,
      type: car.type,
      brand: car.brand,
      kwPower: car.kwPower,
      usdPrice: car.price,
      isRented: car.isRented
    }, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
  }

  update(car: ICar): Observable<EntityResponseType> {
    console.log(car);
    return this.http.put<ICar>(this.carURL, {
      id: car.id,
      type: car.type,
      brand: car.brand,
      kwPower: car.kwPower,
      usdPrice: car.price,
      isRented: car.isRented
    }, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICar>(`${this.carURL}/${id}`, {observe: 'response'});
  }

  query(): void {
    this.http.get<ICar[]>(this.carURL + '?cy=' + CurrencyEnum[this.selectedCurrency.value], {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).subscribe(response => {
      if (response.status === 503) {
        this.dialog.open(ErrorDialogComponent, {
          hasBackdrop: true
        });
      }
      this.loadedCars.next(response.body);
    });
  }

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

  getCar(carId: number): Observable<ICar> {
    return this.http.get<ICar>(`${this.carURL}/${carId}`, {
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
      this.myRentals.next(value);
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
      this.allRentals.next(value);
    });
  }
}
