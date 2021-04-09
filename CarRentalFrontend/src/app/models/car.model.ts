export interface ICar {
  id?: number;
  type?: string;
  brand?: string;
  kwPower?: number;
  price?: number;
  currency?: CurrencyEnum;
  isRented?: boolean;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public type?: string,
    public brand?: string,
    public kwPower?: number,
    public price?: number,
    public currency?: CurrencyEnum,
    public isRented?: boolean
  ) {
    this.isRented = this.isRented || false;
  }
}

export enum CurrencyEnum {
  EUR,
  USD,
  JPY,
  BGN,
  CZK,
  DKK,
  GBP,
  HUF,
  PLN,
  RON,
  SEK,
  CHF,
  ISK,
  NOK,
  HRK,
  RUB,
  TRY,
  AUD,
  BRL,
  CAD,
  CNY,
  HKD,
  IDR,
  ILS,
  INR,
  KRW,
  MXN,
  MYR,
  NZD,
  PHP,
  SGD,
  THB,
  ZAR
}
