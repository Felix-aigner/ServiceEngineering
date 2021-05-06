import {createAction, props} from "@ngrx/store";
import {Car, CurrencyEnum} from "../../models/car.model";
import {Rental} from "../../models/rental.model";


export const GetAllCars = createAction(
  '[Car] Get All Cars',
  props<{currency: CurrencyEnum}>()
);

export const GetAllCarsSuccess = createAction(
  '[Car] Get All Cars Success',
  props<{cars: Car[]}>()
);

export const GetAllRentals = createAction(
  '[Rental] Get All Rentals'
);

export const GetAllRentalsSuccess = createAction(
  '[Rental] Get All Rentals Success',
  props<{rentals: Rental[]}>()
);

export const GetCarById = createAction(
  '[Car] Get Car By Id',
  props<{ id: string }>()
);

export const GetCarByIdSuccess = createAction(
  '[Car] Get Car By Id Success',
  props<{cars: Car}>()
);
