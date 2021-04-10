import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {IsLoggedInGuardService} from './services/login-guard.service';
import {MyCarsComponent} from './components/my-cars/my-cars.component';
import {ContactComponent} from './components/contact/contact.component';

const appRoutes: Routes = [
  {
    path: '',
    component: LoginComponent,
    pathMatch: 'full',
    canActivate: []
  },
  {
    path: 'main',
    component: DashboardComponent,
    canActivate: [IsLoggedInGuardService]
  },
  {
    path: 'mycars',
    component: MyCarsComponent,
    canActivate: [IsLoggedInGuardService]
  },
  {
    path: 'contact',
    component: ContactComponent,
    canActivate: [IsLoggedInGuardService]
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes, {enableTracing: false, useHash: true})
  ],
  exports: [
    RouterModule
  ],
})
export class AppRoutingModule {
}
