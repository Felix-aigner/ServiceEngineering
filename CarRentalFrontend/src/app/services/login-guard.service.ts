import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from './user.service';
import {EMPTY, Observable, of} from 'rxjs';
import {first, switchMap} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class IsLoggedInGuardService implements CanActivate {

  constructor(private userService: UserService,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {

    return this.userService.isLoggedIn
      .pipe(first(),
        switchMap(isLoggedIn => {
          if (!isLoggedIn) {
            this.router.navigate(['']);
            return EMPTY;
          } else {
            return of(true);
          }
        }));
  }

}

