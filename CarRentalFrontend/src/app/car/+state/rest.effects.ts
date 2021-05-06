import {Injectable} from "@angular/core";
import {Actions, Effect, ofType} from "@ngrx/effects";
import {Observable} from "rxjs";
import {Action} from "@ngrx/store";

import * as carActions from "./rest.actions"
import {map, switchMap} from "rxjs/operators";
import {RestService} from "../../services/rest.service";
import {Car} from "../../models/car.model";
import {Rental} from "../../models/rental.model";

@Injectable(
)
export class RestEffects {

  constructor(
    private actions$: Actions,
    private restService: RestService,
  ) {
  }

  @Effect()
  readonly getAllCars$: Observable<Action> = this.actions$.pipe(
    ofType(carActions.GetAllCars),
    switchMap(({currency}) => {
      return this.restService.getAllCars(currency).pipe(
        map((cars: Car[]) => {
          return carActions.GetAllCarsSuccess({cars})
        })
      );
    })
  );

  @Effect()
  readonly getAllRentals$: Observable<Action> = this.actions$.pipe(
    ofType(carActions.GetAllRentals),
    switchMap(() => {
      return this.restService.getAllRentals().pipe(
        map((rentals: Rental[]) => {
          return carActions.GetAllRentalsSuccess({rentals})
        })
      );
    })
  );




}
