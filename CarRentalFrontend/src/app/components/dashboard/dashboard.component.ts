import {Component, OnInit} from '@angular/core';
import {Car} from '../../car/models/car.model';
import {CarService} from '../../services/car.service';
import {UserService} from '../../services/user.service';
import {MatDialog} from '@angular/material/dialog';
import {BehaviorSubject} from 'rxjs';
import {CreateCarComponent} from '../../dialogs/create-car/create-car.component';
import {EditCarComponent} from '../../dialogs/edit-car/edit-car.component';
import {ConfirmationDialogComponent} from '../../dialogs/confirmation-dialog/confirmation-dialog.component';
import {BookingConfirmationComponent} from "../../dialogs/booking-confirmation/booking-confirmation.component";
import * as rest from "../../car/+state/rest.actions";
import {Store} from "@ngrx/store";
import {State} from "../../app.store";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public loadedCars: BehaviorSubject<Car[]> = this.carService.loadedCars;
  confirmedBooking = new BehaviorSubject<boolean>(false);
  confirmedDelete = new BehaviorSubject<boolean>(false);


  constructor(
    private carService: CarService,
    public userService: UserService,
    private dialog: MatDialog,
    private store: Store<State>
  ) {
  }

  ngOnInit(): void {
    this.carService.getCarsFromStore();
    this.store.dispatch(rest.GetAllCars());
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

  openDeleteDialog(carId: number): void {
    this.dialog.open(ConfirmationDialogComponent, {
      data: 'Do you want to delete this Car?',
      hasBackdrop: true
    }).afterClosed()
      .subscribe(result => {
        this.confirmedDelete.next(result);
        if (this.confirmedDelete.value) {
          this.carService.delete(carId).subscribe();
        }
      });
  }
}
