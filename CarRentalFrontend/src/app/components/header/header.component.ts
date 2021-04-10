import {Component, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {CurrencyEnum} from '../../models/car.model';
import {CarService} from '../../services/car.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  selectedCurrency = new FormControl();
  currencyEnum = CurrencyEnum;
  currencies;

  constructor(private carService: CarService) {
    this.currencies = Object.keys(this.currencyEnum).filter(k => !isNaN(Number(k)));
    this.carService.selectedCurrency.subscribe(data => this.selectedCurrency.patchValue(data));
  }

  ngOnInit(): void {
  }


  saveSelectedCurrency(): void {
    this.carService.selectedCurrency.next(this.selectedCurrency.value);
    this.carService.query();
  }

}
