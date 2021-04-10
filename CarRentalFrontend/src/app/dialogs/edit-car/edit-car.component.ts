import {Component, Inject, OnInit} from '@angular/core';
import {Car, CurrencyEnum, ICar} from '../../models/car.model';
import {FormBuilder} from '@angular/forms';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {CarService} from '../../services/car.service';

@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.css']
})
export class EditCarComponent implements OnInit {

  editForm = this.fb.group({
    id: [],
    carType: [],
    brand: [],
    kwPower: [],
    price: [],
    currency: [],
    isRented: [],
  });
  currencyEnum = CurrencyEnum;
  currencies;

  constructor(@Inject(MAT_DIALOG_DATA) public data: Car, private carService: CarService, private fb: FormBuilder) {
    this.currencies = Object.keys(this.currencyEnum).filter(k => !isNaN(Number(k)));
    this.updateForm(data);
    console.log(data.currency);
    console.log(data.price);
  }

  ngOnInit(): void {
  }

  updateForm(car: ICar): void {
    this.editForm.patchValue({
      id: car.id,
      carType: car.type,
      brand: car.brand,
      kwPower: car.kwPower,
      price: car.price,
      currency: car.currency,
      isRented: car.isRented,
    });
  }

  save(): void {
    const car = this.createFromForm();
    this.carService.saveEditCar(car);
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

}
