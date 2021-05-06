import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Car} from '../../models/car.model';
import {FormBuilder} from '@angular/forms';
import {CarService} from '../../services/car.service';
import {Rental} from "../../models/rental.model";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-booking-confirmation',
  templateUrl: './booking-confirmation.component.html',
  styleUrls: ['./booking-confirmation.component.css']
})
export class BookingConfirmationComponent implements OnInit {

  bookingForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    isActive: [true],
    car: [],
  });

  constructor(@Inject(MAT_DIALOG_DATA) public data: Car,
              private fb: FormBuilder,
              private dialog: MatDialogRef<BookingConfirmationComponent>,
              private carService: CarService,
              private userService: UserService
              ) {
    this.updateBooking(data);
  }

  ngOnInit(): void {
  }

  confirmation(booking: boolean): void {
    if (booking) {
      this.carService.bookCar(this.createFromForm());
    }
    this.dialog.close();
  }

  private updateBooking(data: Car): void {
    this.bookingForm.patchValue({
      car: data
    });
  }


  private createFromForm(): Rental {
    return {
      id: this.bookingForm.get(['id'])!.value,
      startDate: this.bookingForm.get(['startDate'])!.value.toString(),
      endDate: this.bookingForm.get(['endDate'])!.value.toString(),
      isActive: this.bookingForm.get(['isActive'])!.value,
      carId: (<Car>(this.bookingForm.get(['car'])!.value)).id,
      userId: this.userService.currUser.value.id
    };
  }
}
