import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HttpClient,} from '@angular/common/http';
import { HttpModule,Http } from '@angular/http';

import {RequestOptions, XHRBackend} from '@angular/http';
import { Router } from '@angular/router';



import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeGuestSearchComponent } from './home-guest-search/home-guest-search.component';
import { ViewAvioCompaniesComponent } from './view-avio-companies/view-avio-companies.component';
import { ViewHotelsComponent } from './view-hotels/view-hotels.component';
import { ViewRentalCarsComponent } from './view-rental-cars/view-rental-cars.component';
import { RegistrationComponent } from './registration/registration.component';


import { AviocompanySService } from './services/aviocompany-s.service';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeGuestSearchComponent,
    ViewAvioCompaniesComponent,
    ViewHotelsComponent,
    ViewRentalCarsComponent,
    RegistrationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [HttpClientModule,AviocompanySService,
  {
        provide: Http,
    
      deps: [XHRBackend, RequestOptions, Router]
    }
      ],
  bootstrap: [AppComponent]
})
export class AppModule { }
