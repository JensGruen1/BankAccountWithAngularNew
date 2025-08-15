import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { NavbarComponent } from '../navbar/navbar.component';
import { AccountNavbarComponent } from '../account-navbar/account-navbar.component';

@Component({
  selector: 'app-home',
  standalone: true, 
  imports: [
    CommonModule,
    FormsModule,
    NavbarComponent,
    AccountNavbarComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {}
