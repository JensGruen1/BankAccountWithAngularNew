import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig, Component, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, Routes } from '@angular/router';
import { SignupComponent } from './features/signup/signup.component';
import {LoginComponent} from './features/login/login.component';

// import { routes } from './app.routes';

// export const appConfig: ApplicationConfig = {
//   providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes)]
// };

const routes: Routes = [
  {path: 'signup', component: SignupComponent},
  //{path: '', redirectTo: 'signup', pathMatch: 'full' }, // Optional, für Root
  {path: 'login', component: LoginComponent},
  {path: '**', redirectTo: 'signup'},
];


export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    provideRouter(routes)
  ]
};
