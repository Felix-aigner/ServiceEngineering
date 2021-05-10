import {NgModule, APP_INITIALIZER} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {AppRoutingModule} from './app-routing.module';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatDialogModule} from '@angular/material/dialog';
import {MatCardModule} from '@angular/material/card';
import {MatTabsModule} from '@angular/material/tabs';
import {CreateCarComponent} from './dialogs/create-car/create-car.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {EditCarComponent} from './dialogs/edit-car/edit-car.component';
import {ConfirmationDialogComponent} from './dialogs/confirmation-dialog/confirmation-dialog.component';
import {MyCarsComponent} from './components/my-cars/my-cars.component';
import {HeaderComponent} from './components/header/header.component';
import {ErrorDialogComponent} from './dialogs/error-dialog/error-dialog.component';
import {ContactComponent} from './components/contact/contact.component';
import {GoogleMapsModule} from '@angular/google-maps';
import {RentalsComponent} from './components/rentals/rentals.component';
import {BookingConfirmationComponent} from './dialogs/booking-confirmation/booking-confirmation.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from "@angular/material/core";
import {EffectsModule} from "@ngrx/effects";
import {RestEffects} from "./car/+state/rest.effects";
import {StoreModule} from "@ngrx/store";
import {reducers} from "./app.store";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {ConfigService} from './services/config.service';

export const configFactory = (configService: ConfigService) => {
  return () => configService.loadConfig();
};

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    CreateCarComponent,
    EditCarComponent,
    ConfirmationDialogComponent,
    MyCarsComponent,
    HeaderComponent,
    ErrorDialogComponent,
    ContactComponent,
    RentalsComponent,
    BookingConfirmationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatCardModule,
    MatTabsModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    GoogleMapsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    EffectsModule.forRoot([RestEffects]),
    [StoreModule.forRoot(reducers)],
    StoreDevtoolsModule.instrument({
      name: 'CarShare',
      maxAge: 25,
    }),

  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: configFactory,
      deps: [ConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [ConfirmationDialogComponent, CreateCarComponent, EditCarComponent, ErrorDialogComponent, BookingConfirmationComponent]
})
export class AppModule {
}
