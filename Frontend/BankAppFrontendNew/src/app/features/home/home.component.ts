import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true, 
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  // user = {
  //   username: '',
  //   password: '',
  // };


  // constructor(private http: HttpClient, private router: Router) {}


  //   onLogout() {
  //   this.http.post('http://localhost:8080/api/users/logout', this.user, { responseType: 'text' }).subscribe({
  //     next: (response) => {
  //       setTimeout(() => {
  //         this.router.navigate(['/login']);
  //       }, 1000);
  
  //     },
  //   });
  // }



}
