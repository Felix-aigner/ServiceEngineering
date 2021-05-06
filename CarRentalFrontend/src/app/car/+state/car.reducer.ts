import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";
import {Car} from "../../models/car.model";
import {Action, createReducer, on} from "@ngrx/store";

import * as restAction from './rest.actions';

export const carAdapter: EntityAdapter<Car> = createEntityAdapter<Car>({
  selectId: vp => vp.id
});

export interface CarState extends EntityState<Car> {
}

export const initialState: CarState = carAdapter.getInitialState({});

export function reducer(state: CarState | undefined, action: Action): CarState {
  return CarReducer(state, action);
}

const CarReducer = createReducer(
  initialState,
  on(restAction.GetAllCarsSuccess,
    (state, {cars}) => ({
      ...carAdapter.upsertMany(cars, state)
    })),
);

export const {
  selectAll: selectAllCars,
  selectIds,
  selectTotal,
} = carAdapter.getSelectors();

export function getAllCars(): (s: CarState) => Car[] {
  return (state: CarState) => selectAllCars(state)
}
