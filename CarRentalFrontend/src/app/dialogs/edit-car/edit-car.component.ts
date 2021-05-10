import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {Car} from '../../models/car.model';
import {FormBuilder} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CarService} from '../../services/car.service';
import {Subject} from 'rxjs';
import * as restAction from "../../car/+state/rest.actions";
import {State} from "../../app.store";
import {Store} from "@ngrx/store";
import {UserService} from "../../services/user.servizz";

@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.css']
})
export class EditCarComponent implements OnInit, OnDestroy {

  editForm = this.fb.group({
    id: [],
    type: [],
    brand: [],
    kwPower: [],
    price: [],
    isRented: [],
  });

  private unsubscribe$ = new Subject();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Car,
    private carService: CarService,
    private fb: FormBuilder,
    private dialog: MatDialogRef<EditCarComponent>,
    private store: Store<State>
  ) {
    this.updateForm(data);
  }

  ngOnInit(): void {
  }

  updateForm(car: Car): void {
    this.editForm.patchValue({
      id: car.id,
      type: car.type,
      brand: car.brand,
      kwPower: car.kwPower,
      price: car.price,
    });
  }

  save(): void {
    const car = this.createFromForm();
    this.carService.updateCar(car).subscribe( _ => this.dialog.close());
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
