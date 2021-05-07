import {Component, OnDestroy, OnInit} from '@angular/core';
import {of, Subject, zip} from 'rxjs';
import {CarService} from '../../services/car.service';
import {Rental, RentalCar} from '../../models/rental.model';
import {RentalSelectorService} from "../../car/rental-selector.service";
import {map, switchMap} from "rxjs/operators";
import {UserService} from "../../services/user.service";
import {CarSelectorService} from "../../car/car-selector.service";
import {Car} from "../../models/car.model";

@Component({
  selector: 'app-my-cars',
  templateUrl: './my-cars.component.html',
  styleUrls: ['./my-cars.component.css']
})
export class MyCarsComponent implements OnInit, OnDestroy {

  public myRentals: RentalCar[];

  private unsubscribe$ = new Subject();

  constructor(public carService: CarService,
              private rentalSelector: RentalSelectorService,
              private carSelector: CarSelectorService,
              private userService: UserService
  ) {
  }

  ngOnInit(): void {
    zip(
      this.rentalSelector.getRentalsForUserIdFromStore(this.userService.currUser.value.id),
      this.carSelector.getAllCarsFromStore()
    ).pipe(
      switchMap(([rentals, cars]: [Rental[], Car[]]) => {
        return of(rentals.map(rental => {
          return <RentalCar>{
            ...rental,
            car: cars.filter((car: Car) => car.id === rental.carId)
          }
        }))
      })
    ).subscribe((rentals: RentalCar[]) => {
      this.myRentals = rentals
    })
  }


  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  releaseCar(rental: RentalCar): void {
    this.carService.releaseCar(<Rental>{
      id: rental.id,
      startDate: rental.startDate,
      endDate: rental.endDate,
      isActive: rental.isActive,
      carId: rental.carId,
      userId: rental.userId
    });
  }
}
