import { Injectable } from '@angular/core';
import {createSelector, select, Store} from "@ngrx/store";
import * as fromRoot from '../app.store';
import {Observable} from "rxjs";
import {Car} from "../models/car.model";
import * as fromCar from './+state/car.reducer';

@Injectable({
  providedIn: 'root'
})
export class CarSelectorService {

  constructor(private store: Store<fromRoot.State>) { }


  getAllCarsFromStore(): Observable<Car[]> {
    return this.store.pipe(
      select(createSelector(
        fromRoot.getCarState, fromCar.getAllCars())
      )
    );
  }


}
