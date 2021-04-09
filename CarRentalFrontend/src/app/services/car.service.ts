import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL} from '../app.constants';
import {ICar} from '../models/car.model';
import {UserService} from "./user.service";

type EntityResponseType = HttpResponse<ICar>;
type EntityArrayResponseType = HttpResponse<ICar[]>;

@Injectable({ providedIn: 'root' })
export class CarService {
  public resourceUrl = SERVER_API_URL + 'cars';

  private readonly commonHttpHeaders;

  constructor(protected http: HttpClient, private userService: UserService) {
    this.commonHttpHeaders = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json');
  }

  create(car: ICar): Observable<EntityResponseType> {
    return this.http.post<ICar>(this.resourceUrl, car, { observe: 'response' });
  }

  update(car: ICar): Observable<EntityResponseType> {
    return this.http.put<ICar>(this.resourceUrl, car, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(): Observable<EntityArrayResponseType> {
    return this.http.get<ICar[]>(this.resourceUrl + '/list', {observe: 'response' , headers: this.commonHttpHeaders.append('token', this.userService.currUser.value.token)});
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
