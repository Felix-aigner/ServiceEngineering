import {Component, OnInit} from '@angular/core';
import {Car, ICar} from '../../models/car.model';
import {CarService} from '../../services/car.service';
import {HttpResponse} from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

 public cars: Car[];

  constructor(private carService: CarService) {
  }

  ngOnInit(): void {
    this.carService.query().subscribe((res: HttpResponse<ICar[]>) => (this.cars = res.body || []));
  }


}
