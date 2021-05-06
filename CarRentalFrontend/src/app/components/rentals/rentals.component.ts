import {Component, OnInit} from '@angular/core';
import {CarService} from '../../services/car.service';
import {Rental} from '../../models/rental.model';
import {UserService} from '../../services/user.service';
import {RentalSelectorService} from "../../car/rental-selector.service";

@Component({
  selector: 'app-rentals',
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {

  allrentals: Rental[]

  constructor(
    private carService: CarService,
    private userService: UserService,
    private rentalSelector: RentalSelectorService
  ) {

  }

  ngOnInit(): void {
    this.rentalSelector.getAllRentalsFromStore()
      .subscribe((rentals: Rental[]) => this.allrentals = rentals)
  }

  releaseCar(rental: Rental): void {
    this.carService.releaseCar(rental);
  }
}
