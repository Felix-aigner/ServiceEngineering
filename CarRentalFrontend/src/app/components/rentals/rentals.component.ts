import {Component, OnInit} from '@angular/core';
import {CarService} from '../../services/car.service';
import {Rental, RentalCar} from '../../models/rental.model';
import {UserService} from '../../services/user.service';
import {RentalSelectorService} from "../../car/rental-selector.service";
import {of, zip} from "rxjs";
import {switchMap} from "rxjs/operators";
import {Car} from "../../models/car.model";
import {CarSelectorService} from "../../car/car-selector.service";

@Component({
  selector: 'app-rentals',
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {

  allrentals: RentalCar[];

  constructor(
    private carService: CarService,
    private userService: UserService,
    private rentalSelector: RentalSelectorService,
    private carSelector: CarSelectorService,
  ) {

  }

  ngOnInit(): void {
    zip(
      this.rentalSelector.getAllRentalsFromStore(),
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
      this.allrentals = rentals
    })
  }
}
