import {ICar} from './car.model';


export interface IRental {
  id?: number;
  startDate?: string;
  endDate?: string;
  isActive?: boolean;
  car?: ICar;
}

export class Rental implements IRental {
  constructor(
    id?: number,
    startDate?: string,
    endDate?: string,
    isAvtice?: boolean,
    car?: ICar
  ) {
  }
}
