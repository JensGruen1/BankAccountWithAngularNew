import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 

@Component({
  selector: 'app-account-navbar',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './account-navbar.component.html',
  styleUrl: './account-navbar.component.css'
})
export class AccountNavbarComponent {

  user = {
  username: '',
  password: '',
};

constructor(private http: HttpClient, private router: Router) {}


onNewAccount () {
  
}

}
