import {Car} from "./car.model";

export interface Rental {
  id?: string;
  startDate?: string;
  endDate?: string;
  isActive?: boolean;
  carId: string,
  userId: string,
}

export interface RentalCar extends Rental{
  car: Car
}
