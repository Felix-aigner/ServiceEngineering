import {Component, OnInit} from '@angular/core';
import {CarService} from '../../services/car.service';
import {IRental} from '../../models/rental.model';

@Component({
  selector: 'app-rentals',
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.css']
})
export class RentalsComponent implements OnInit {

  allrentals = this.carService.allRentals;

  constructor(private carService: CarService) {
    this.carService.queryAllRentals();
  }

  ngOnInit(): void {
  }

  releaseCar(rental: IRental): void {
    this.carService.releaseCar(rental);
  }
}
