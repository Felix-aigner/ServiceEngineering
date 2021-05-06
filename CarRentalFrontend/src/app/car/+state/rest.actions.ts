import {createAction, props} from "@ngrx/store";
import {Car} from "../models/car.model";


export const GetAllCars = createAction(
  '[Car] Get All Cars'
);

export const GetAllCarsSuccess = createAction(
  '[Car] Get All Cars Success',
  props<{cars: Car[]}>()
);

export const GetCarById = createAction(
  '[Car] Get Car By Id',
  props<{ id: string }>()
);

export const GetCarByIdSuccess = createAction(
  '[Car] Get Car By Id Success',
  props<{cars: Car}>()
);
