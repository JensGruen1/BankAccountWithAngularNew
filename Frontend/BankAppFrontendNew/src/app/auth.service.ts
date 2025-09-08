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


  //without jwt
  // checkUser() {
  //   this.http.get('/api/users/isUserLoggedIn').subscribe({
  //     next: user => this.currentUser = user,
  //     error: err => {
  //       this.logout();
  //     }
  //   });
  // }


  //with jwt
  checkUser() {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    this.currentUser = this.decodeToken(token); // optional, falls du Username o. Rollen brauchst
  } else {
    this.logout();
  }
}
  //without jwt
  // logout() {
  //   this.http.post('http://localhost:8080/api/users/logout',{}, {withCredentials: true, responseType: 'text' }).subscribe({
  //     next: () => {
  //        //this.successMessage = response;
  //        localStorage.clear();
  //        setTimeout(() => {
  //         this.router.navigate(['/login'], { queryParams: { logout: true } });
  //       }, 1000);
  //     },
  //   });
  // }

  //with jwt
  logout() {
  localStorage.removeItem('jwt_token'); // 🔹 nur Token löschen
  this.currentUser = null;
  this.router.navigate(['/login'], { queryParams: { logout: true } });
}

  //without jwt
  // logoutInactivity() {
  //   this.http.post('http://localhost:8080/api/users/logout',{}, {withCredentials: true, responseType: 'text' }).subscribe({
  //     next: () => {
  //        //this.successMessage = response;
  //        localStorage.clear();
  //        setTimeout(() => 
  //         this.router.navigate(['/login']), 1000);
  //     },
  //   });
  // }


  //with jwt
  logoutInactivity() {
  this.logout();
}


   //only with jwt
  decodeToken(token: string) {
    try {
      return jwt_decode(token);
    } catch (e) {
      console.error('Invalid token', e);
      return null;
    }
  }



}

function jwt_decode(token: string) {
  throw new Error('Function not implemented.');
}
