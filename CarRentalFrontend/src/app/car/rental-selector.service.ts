import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {createSelector, select, Store} from "@ngrx/store";
import * as fromRoot from "../app.store";
import * as fromRental from "./+state/rental.reducer";
import {Rental} from "../models/rental.model";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RentalSelectorService {

  constructor(private store: Store<fromRoot.State>) { }

  getAllActiveRentalsFromStore(): Observable<Rental[]> {
    return this.store.pipe(
      select(createSelector(
        fromRoot.getRentalState, fromRental.getAllRentals())
      )
    ).pipe(
      map((rentals) => rentals.filter(rental => rental.isActive))
    );
  }

  getAllRentalsFromStore(): Observable<Rental[]> {
    return this.store.pipe(
      select(createSelector(
        fromRoot.getRentalState, fromRental.getAllRentals())
      )
    )
  }

  getRentalsForUserIdFromStore(id: string): Observable<Rental[]> {
    return this.store.pipe(
      select(createSelector(
        fromRoot.getRentalState, fromRental.getRentalsForUserId(id))
      )
    )
  }
}
