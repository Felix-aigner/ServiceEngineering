import {Component, OnInit} from '@angular/core';
import {Car, CurrencyEnum, ICar} from '../../models/car.model';
import {CarService} from '../../services/car.service';
import {HttpResponse} from '@angular/common/http';
import {UserService} from '../../services/user.service';
import {MatDialog} from '@angular/material/dialog';
import {BehaviorSubject} from 'rxjs';
import {CreateCarComponent} from '../../dialogs/create-car/create-car.component';
import {FormControl} from '@angular/forms';
import {EditCarComponent} from '../../dialogs/edit-car/edit-car.component';
import {ConfirmationDialogComponent} from "../../dialogs/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public cars: Car[];
  confirmedBooking = new BehaviorSubject<boolean>(false);
  selectedCurrency = new FormControl();
  currencyEnum = CurrencyEnum;
  currencies;

  constructor(private carService: CarService, public userService: UserService, private dialog: MatDialog) {
    this.currencies = Object.keys(this.currencyEnum).filter(k => !isNaN(Number(k)));
  }

  ngOnInit(): void {
    this.carService.query().subscribe((res: HttpResponse<ICar[]>) => (this.cars = res.body || []));
  }

  saveSelectedCurrency(): void {
    console.log(this.carService.selectedCurrency.value);
    this.carService.selectedCurrency.next(this.selectedCurrency.value);
    console.log(this.carService.selectedCurrency.value);
    this.carService.query().subscribe((res: HttpResponse<ICar[]>) => (this.cars = res.body || []));
  }


  openBookingDialog(): void {
    this.dialog.open(ConfirmationDialogComponent, {
      data: 'Do you want to book this Car?',
      hasBackdrop: true
    }).afterClosed()
      .subscribe(result => {
        this.confirmedBooking.next(result);
        if (this.confirmedBooking.value) {
          // book car
        }
      });
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

  openDeleteDialog(car: Car): void {
    this.dialog.open(ConfirmationDialogComponent, {
      data: 'Do you want to delete this Car?',
      hasBackdrop: true
    }).afterClosed()
      .subscribe(result => {
        this.confirmedBooking.next(result);
        if (this.confirmedBooking.value) {
          // book car
        }
      });
  }
}
