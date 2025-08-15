import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true, 
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
    user = {
    username: '',
    password: '',
  };

 errorMessage ='';
 successMessage = '';

constructor(private http: HttpClient, private router: Router) {}


  onSubmit() {
    this.http.post('http://localhost:8080/api/users/login', this.user, { withCredentials: true, responseType: 'text' }).subscribe({
      next: (response) => {
        this.successMessage = response;
        localStorage.setItem('user', this.user.username);
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
  
      },
      error: (error) => {
        if (error.status === 0) {
          // Status 0 = kein Kontakt zum Backend
          this.errorMessage = '❌ Keine Verbindung zum Server';
        } else if (error.status === 401) {
          // Status 401 = Login falsch
          this.errorMessage = '❌ Ungültige Anmeldedaten';
        } else {
          // Alle anderen HTTP-Fehler
          this.errorMessage = `❌ Fehler vom Server (${error.status}): ${error.statusText || 'Unbekannt'}`;
        }
        this.clearMessagesAfterDelay();
      },
    });
  }


  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 10000);
  }
  
}
