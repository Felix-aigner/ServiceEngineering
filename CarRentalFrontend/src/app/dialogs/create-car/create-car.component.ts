import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {CarService} from '../../services/car.service';
import {Car, CurrencyEnum, ICar} from '../../models/car.model';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {Subject} from "rxjs";

@Component({
  selector: 'app-create-car',
  templateUrl: './create-car.component.html',
  styleUrls: ['./create-car.component.css']
})
export class CreateCarComponent implements OnInit, OnDestroy {

  editForm = this.fb.group({
    id: [],
    type: [],
    brand: [],
    kwPower: [],
    price: [],
    currency: [],
    isRented: [],
  });

  currencyEnum = CurrencyEnum;
  currencies;

  private unsubscribe$ = new Subject();

  constructor(@Inject(MAT_DIALOG_DATA) inputData: ICar, private carService: CarService, private fb: FormBuilder) {
    this.currencies = Object.keys(this.currencyEnum).filter(k => !isNaN(Number(k)));
  }

  ngOnInit(): void {
  }

  save(): void {
    const car = this.createFromForm();
    this.carService.save(car);
  }

  private createFromForm(): ICar {
    return {
      ...new Car(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      brand: this.editForm.get(['brand'])!.value,
      kwPower: this.editForm.get(['kwPower'])!.value,
      price: this.editForm.get(['price'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      isRented: this.editForm.get(['isRented'])!.value,
    };
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
