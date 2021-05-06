export interface Car {
  id?: number;
  type?: string;
  brand?: string;
  kwPower?: number;
  price?: number;
  currency?: CurrencyEnum;
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
