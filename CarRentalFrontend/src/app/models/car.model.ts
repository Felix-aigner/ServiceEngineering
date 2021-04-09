export interface ICar {
  id?: number;
  carType?: string;
  brand?: string;
  kwPower?: number;
  usdPrice?: number;
  isRented?: boolean;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public carType?: string,
    public brand?: string,
    public kwPower?: number,
    public usdPrice?: number,
    public isRented?: boolean
  ) {
    this.isRented = this.isRented || false;
  }
}
