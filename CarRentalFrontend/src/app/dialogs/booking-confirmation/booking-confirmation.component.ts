import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ICar} from '../../models/car.model';
import {FormBuilder} from '@angular/forms';
import {CarService} from '../../services/car.service';
import {IRental, Rental} from '../../models/rental.model';

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

  constructor(@Inject(MAT_DIALOG_DATA) public data: ICar,
              private fb: FormBuilder,
              private dialog: MatDialogRef<BookingConfirmationComponent>,
              private carService: CarService) {
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

  private updateBooking(data: ICar): void {
    this.bookingForm.patchValue({
      car: data
    });
  }


  private createFromForm(): IRental {
    return {
      ...new Rental(),
      id: this.bookingForm.get(['id'])!.value,
      startDate: this.bookingForm.get(['startDate'])!.value,
      endDate: this.bookingForm.get(['endDate'])!.value,
      isActive: this.bookingForm.get(['isActive'])!.value,
      car: this.bookingForm.get(['car'])!.value,
    };
  }
}
