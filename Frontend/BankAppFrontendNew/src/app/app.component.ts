import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { InactivityService } from './app.inactivityService';
import { AccountNavbarComponent } from './features/account-navbar/account-navbar.component';
import { filter } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { HomeComponent } from './features/home/home.component';
import { NavbarComponent } from './features/navbar/navbar.component';
import { ShowTransfersComponent } from './features/show-transfers/show-transfers.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    AccountNavbarComponent,
    CommonModule,
    FormsModule,
    HomeComponent,
    NavbarComponent,
    ShowTransfersComponent
  ],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    showNavbar = false;
    private inactivityService = inject(InactivityService);
     private router = inject(Router);
    selectedAccountNumber!:string;


    // Wird aufgerufen, wenn eine Route geladen wird
  onActivate(component: any) {
    // Navbar nur anzeigen, wenn HomeScreenComponent aktiv ist
    console.log('Activated component:', component.constructor.name);
    this.showNavbar = component instanceof HomeComponent;
  }


  onAccountChanged(accountNumber: string) {
    this.selectedAccountNumber = accountNumber;
  }
}
