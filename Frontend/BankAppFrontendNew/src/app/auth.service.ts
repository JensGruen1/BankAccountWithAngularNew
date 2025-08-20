import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentUser: any;
  successMessage = '';

  constructor(private http: HttpClient, private router: Router) {}

  checkUser() {
    this.http.get('/api/users/isUserLoggedIn').subscribe({
      next: user => this.currentUser = user,
      error: err => {
        this.logout();
      }
    });
  }

  logout() {
    this.http.post('http://localhost:8080/api/users/logout',{}, {withCredentials: true, responseType: 'text' }).subscribe({
      next: () => {
         //this.successMessage = response;
         localStorage.clear();
         setTimeout(() => {
          this.router.navigate(['/login'], { queryParams: { logout: true } });
        }, 1000);
      },
    });
  }


  logoutInactivity() {
    this.http.post('http://localhost:8080/api/users/logout',{}, {withCredentials: true, responseType: 'text' }).subscribe({
      next: () => {
         //this.successMessage = response;
         localStorage.clear();
         setTimeout(() => 
          this.router.navigate(['/login']), 1000);
      },
    });
  }



}