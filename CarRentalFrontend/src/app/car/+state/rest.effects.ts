import {Injectable} from "@angular/core";
import {Actions, Effect, ofType} from "@ngrx/effects";
import {CarService} from "../../services/car.service";
import {Observable} from "rxjs";
import {Action} from "@ngrx/store";

import * as carActions from "./rest.actions"
import {map, switchMap} from "rxjs/operators";
import {RestService} from "../../services/rest.service";
import {Car} from "../models/car.model";

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
    switchMap(() => {
      return this.restService.getAllCars().pipe(
        map((cars: Car[]) => {
          return carActions.GetAllCarsSuccess({cars})
        })
      );
    })
  );





}
