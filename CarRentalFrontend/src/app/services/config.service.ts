import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {UserService} from "./user.servizz";
import {catchError, map} from "rxjs/operators";
import {CurrencyEnum} from "../models/car.model";

export interface Config {
  restUrl:      string;
  currencyUrl:  string;
}

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  config: Config;

  constructor(private http: HttpClient) {}

  loadConfig() {
    return this.http
      .get<Config>('./assets/public-config.json', {},)
      .toPromise()
      .then(config => {
        this.config = config;
      });
  }
}
