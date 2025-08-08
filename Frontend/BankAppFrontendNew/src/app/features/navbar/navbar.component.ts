import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

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


 constructor(private http: HttpClient, private router: Router) {}


  onLogout() {
    console.log('Logout wird ausgeführt');
  this.http.post('http://localhost:8080/api/users/logout', {}, { responseType: 'text' }).subscribe({
  next: () => {
    localStorage.removeItem('user');
     setTimeout(() => {
     console.log("Logout successful");
      this.router.navigate(['/login']);
   }, 1000);
  
  },
  });
 }

 testClick() {
  console.log("Button wurde geklickt");
  alert("Button wurde geklickt");
}


}
