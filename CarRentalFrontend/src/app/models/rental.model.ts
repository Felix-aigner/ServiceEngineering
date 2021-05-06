import {Car} from '../car/models/car.model';


export interface IRental {
  id?: number;
  startDate?: string;
  endDate?: string;
  isActive?: boolean;
  car?: Car;
}

export class Rental implements IRental {
  constructor(
    id?: number,
    startDate?: string,
    endDate?: string,
    isAvtice?: boolean,
    car?: Car
  ) {
  }
}
