import {Component, OnInit} from '@angular/core';
// @ts-ignore
import {Car, CurrencyEnum} from '../../models/car.model';
import {CarService} from '../../services/car.service';
import {UserService} from '../../services/user.servizz';
import {MatDialog} from '@angular/material/dialog';
import {BehaviorSubject, combineLatest, Observable, of, zip} from 'rxjs';
import {CreateCarComponent} from '../../dialogs/create-car/create-car.component';
import {EditCarComponent} from '../../dialogs/edit-car/edit-car.component';
import {ConfirmationDialogComponent} from '../../dialogs/confirmation-dialog/confirmation-dialog.component';
import {BookingConfirmationComponent} from "../../dialogs/booking-confirmation/booking-confirmation.component";
import * as rest from "../../car/+state/rest.actions";
import {Store} from "@ngrx/store";
import {State} from "../../app.store";
import {CarSelectorService} from "../../car/car-selector.service";
import {RentalSelectorService} from "../../car/rental-selector.service";
import {switchMap} from "rxjs/operators";
import {Rental} from "../../models/rental.model";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public loadedCars: Car[];
  confirmedBooking = new BehaviorSubject<boolean>(false);
  confirmedDelete = new BehaviorSubject<boolean>(false);


  constructor(
    private carService: CarService,
    public userService: UserService,
    private dialog: MatDialog,
    private store: Store<State>,
    private carSelector: CarSelectorService,
    private rentalSelector: RentalSelectorService
  ) {
  }

  ngOnInit(): void {
    combineLatest(
      this.carSelector.getAllCarsFromStore(),
      this.rentalSelector.getAllActiveRentalsFromStore()
      ).pipe(
        switchMap(([cars, rentals]: [Car[], Rental[]]) => {
          return of(cars.filter((car: Car) => !rentals.map((rental) => rental.carId).includes(car.id)))
        }
        )).subscribe( (cars: Car[]) => this.loadedCars = cars)
  }


  openNewCarDialog(): void {
    this.dialog.open(CreateCarComponent, {
      hasBackdrop: true
    });
  }

  openEditDialog(car: Car): void {
    this.dialog.open(EditCarComponent, {
      hasBackdrop: true,
      data: car
    });
  }

  openBookingDialog(car: Car): void {
    this.dialog.open(BookingConfirmationComponent, {
      data: car,
      hasBackdrop: true
    });
  }

  openDeleteDialog(carId: string): void {
    this.dialog.open(ConfirmationDialogComponent, {
      data: 'Do you want to delete this Car?',
      hasBackdrop: true
    }).afterClosed()
      .subscribe(result => {
        this.confirmedDelete.next(result);
        if (this.confirmedDelete.value) {
          this.carService.delete(carId);
        }
      });
  }
}
