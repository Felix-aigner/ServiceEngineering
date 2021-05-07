import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {CarService} from '../../services/car.service';
import {Car, CurrencyEnum} from '../../models/car.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Subject} from 'rxjs';

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
    isRented: [],
  });

  currencyEnum = CurrencyEnum;
  currencies;

  private unsubscribe$ = new Subject();

  constructor(
    @Inject(MAT_DIALOG_DATA) inputData: Car,
    private carService: CarService,
    private fb: FormBuilder,
    private dialog: MatDialogRef<CreateCarComponent>,
    ) {
  }

  ngOnInit(): void {
  }

  save(): void {
    const car = this.createFromForm();
    this.carService.createNewCar(car).subscribe(_ => {
      this.dialog.close();
    });
  }

  private createFromForm(): Car {
    return {
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      brand: this.editForm.get(['brand'])!.value,
      kwPower: this.editForm.get(['kwPower'])!.value,
      price: this.editForm.get(['price'])!.value
    };
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
