import {createEntityAdapter, EntityAdapter, EntityState} from "@ngrx/entity";

import {Action, createReducer, on} from "@ngrx/store";

import * as restAction from './rest.actions';
import {Rental} from "../../models/rental.model";

export const rentalAdapter: EntityAdapter<Rental> = createEntityAdapter<Rental>({
  selectId: vp => vp.id
});

export interface RentalState extends EntityState<Rental> {
}

export const initialState: RentalState = rentalAdapter.getInitialState({});

export function reducer(state: RentalState | undefined, action: Action): RentalState {
  return RentalReducer(state, action);
}

const RentalReducer = createReducer(
  initialState,
  on(restAction.GetAllRentalsSuccess,
    (state, {rentals}) => ({
      ...rentalAdapter.upsertMany(rentals, state)
    })),
);

export const {
  selectAll: selectAllRentals,
  selectIds,
  selectTotal,
} = rentalAdapter.getSelectors();

export function getAllRentals(): (s: RentalState) => Rental[] {
  return (state: RentalState) => selectAllRentals(state)
}
