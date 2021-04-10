import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from 'rxjs';
import {CarService} from '../../services/car.service';
import {IRental} from '../../models/rental.model';

@Component({
  selector: 'app-my-cars',
  templateUrl: './my-cars.component.html',
  styleUrls: ['./my-cars.component.css']
})
export class MyCarsComponent implements OnInit, OnDestroy {

  public myRentals = this.carService.myRentals;

  private unsubscribe$ = new Subject();

  constructor(public carService: CarService) {
  }

  ngOnInit(): void {
  }


  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  releaseCar(rental: IRental): void {
    this.carService.releaseCar(rental);
  }
}
