import {Component, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
// @ts-ignore
import {CurrencyEnum} from '../../models/car.model';
import {CarService} from '../../services/car.service';
import {UserService} from "../../services/user.service";
import {Store} from "@ngrx/store";
import {State} from "../../app.store";
import * as rest from "../../car/+state/rest.actions";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  selectedCurrency = new FormControl();
  currencyEnum = CurrencyEnum;
  currencies;

  constructor(private carService: CarService,
              public userService: UserService,
              public store: Store<State>
  ) {
    this.currencies = Object.keys(this.currencyEnum).filter(k => !isNaN(Number(k)));
    this.carService.selectedCurrency.subscribe(data => this.selectedCurrency.patchValue(data));
  }

  ngOnInit(): void {
  }


  saveSelectedCurrency(): void {
    this.carService.selectedCurrency.next(this.selectedCurrency.value);
    this.store.dispatch(rest.GetAllCars({currency: this.selectedCurrency.value}));
  }

}
