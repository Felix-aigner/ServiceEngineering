import {ActionReducerMap, createFeatureSelector, MetaReducer} from '@ngrx/store';
import {storeFreeze} from 'ngrx-store-freeze';
import * as fromCar from './car/+state/car.reducer';
import * as fromRental from './car/+state/rental.reducer';
import {environment} from "../environments/environment";

export interface State {
  car: fromCar.CarState;
  rental: fromRental.RentalState;
}


export const reducers: ActionReducerMap<State> = {
  car: fromCar.reducer,
  rental: fromRental.reducer
};

export const metaReducers: MetaReducer<State>[] = !(environment.production)
  ? [storeFreeze]
  : [];


export const getCarState = createFeatureSelector<fromCar.CarState>('car');
