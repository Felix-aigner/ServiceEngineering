import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';

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
    // canActivate: [IsLoggedInGuardService]
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
