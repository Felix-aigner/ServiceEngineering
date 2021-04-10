import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

import {SERVER_API_URL} from '../app.constants';
import {CurrencyEnum, ICar} from '../models/car.model';
import {UserService} from './user.service';

type EntityResponseType = HttpResponse<ICar>;
type EntityArrayResponseType = HttpResponse<ICar[]>;

@Injectable({providedIn: 'root'})
export class CarService {
  public resourceUrl = SERVER_API_URL + 'cars';

  private readonly commonHttpHeaders;
  selectedCurrency = new BehaviorSubject<CurrencyEnum>(CurrencyEnum.EUR);

  public loadedCars = new BehaviorSubject<ICar[]>([]);

  constructor(protected http: HttpClient, private userService: UserService) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json');
  }

  save(car: ICar): Observable<EntityResponseType> {
    console.log(car);
    return this.http.post<ICar>(this.resourceUrl + '/add', car, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
  }

  update(car: ICar): Observable<EntityResponseType> {
    console.log(car);
    return this.http.post<ICar>(this.resourceUrl + '/change', car, {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICar>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  // add selected currency to request parameters
  query(): void {
    this.http.get<ICar[]>(this.resourceUrl + '/list?cy=' + CurrencyEnum[this.selectedCurrency.value], {
      observe: 'response',
      headers: this.commonHttpHeaders
        .append('token', this.userService.currUser.value.token)
    }).subscribe(response => {
      this.loadedCars.next(response.body);
    });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  bookCar(carId: number) {

  }
}
