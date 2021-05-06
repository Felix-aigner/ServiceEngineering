import {Component, OnDestroy, OnInit} from '@angular/core';
import {of, Subject} from 'rxjs';
import {CarService} from '../../services/car.service';
import {Rental} from '../../models/rental.model';
import {RentalSelectorService} from "../../car/rental-selector.service";
import {map, switchMap} from "rxjs/operators";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-my-cars',
  templateUrl: './my-cars.component.html',
  styleUrls: ['./my-cars.component.css']
})
export class MyCarsComponent implements OnInit, OnDestroy {

  public myRentals: Rental[];

  private unsubscribe$ = new Subject();

  constructor(public carService: CarService,
              private rentalSelector: RentalSelectorService,
              private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.rentalSelector.getAllRentalsFromStore().pipe(
      switchMap((rentals: Rental[]) => {
        return of(rentals.filter((rental: Rental) => rental.userId == this.userService.currUser.value.id))
      })
    ).subscribe((rentals: Rental[]) => this.myRentals = rentals)
  }


  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  releaseCar(rental: Rental): void {
    this.carService.releaseCar(rental);
  }
}
