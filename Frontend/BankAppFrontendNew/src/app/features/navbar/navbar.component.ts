import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true, 
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

user = {
  username: '',
  password: '',
};


 constructor(private http: HttpClient, private router: Router, private authService: AuthService) {}


 onLogout() {
   console.log('Logout wird ausgeführt');
   this.authService.logout();

 }



}
