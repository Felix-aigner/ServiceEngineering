import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {BehaviorSubject, EMPTY} from 'rxjs';
import {UserService} from '../../services/user.service';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {UserModel} from '../../models/user.model';
import {Router} from '@angular/router';
import * as rest from "../../car/+state/rest.actions";
import {CurrencyEnum} from "../../models/car.model";
import {Store} from "@ngrx/store";
import {State} from "../../app.store";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usernameControl = new FormControl('', [Validators.required]);
  passwordControl = new FormControl('', [Validators.required]);
  registerGroup: FormGroup;
  loginError = new BehaviorSubject<boolean>(false);
  loginErrorMessage = new BehaviorSubject<string>('');

  constructor(
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private store: Store<State>
  ) {
    this.registerGroup = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  isBlank(): boolean {
    return this.usernameControl.invalid || this.passwordControl.invalid;
  }

  // tslint:disable-next-line:typedef
  login() {
    this.userService.login(this.usernameControl.value, this.passwordControl.value)
      .pipe(
        catchError((err: HttpErrorResponse) => {
        this.loginError.next(true);
        this.loginErrorMessage.next(err.error);
        this.userService.isLoggedIn.next(false);
        this.router.navigate(['']);
        return EMPTY;
      }))
      .subscribe((token: UserModel) => {

        console.log(token);
        this.loginError.next(false);
        this.loginErrorMessage.next('');
        this.userService.isLoggedIn.next(true);
        this.userService.currUser.next(token);
        this.store.dispatch(rest.GetAllCars({currency: CurrencyEnum.USD}));
        this.store.dispatch(rest.GetAllRentals());
        this.router.navigate(['/main']);
      });
  }

  // tslint:disable-next-line:typedef
  register() {
    this.userService.register(
      this.registerGroup.value.firstname,
      this.registerGroup.value.lastname,
      this.registerGroup.value.username,
      this.registerGroup.value.password
    ).pipe(
        catchError((err: HttpErrorResponse) => {
      this.loginError.next(true);
      this.loginErrorMessage.next(err.error);
      return EMPTY;
    }))
      .subscribe(() => {
        this.loginError.next(false);
        this.loginErrorMessage.next('');
        this.router.navigate(['']);
      });
  }


  ngOnInit(): void {
  }

}
