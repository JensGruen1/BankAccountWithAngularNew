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
    this.http.post('http://localhost:8080/api/users/login', this.user, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.successMessage = response;
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
  
      },
      error: (error) => {
          this.errorMessage = error.status === 401
            ? 'Ungültige Anmeldedaten'
            : 'Ein Fehler ist aufgetreten';
        this.clearMessagesAfterDelay();
      },
    });
  }


  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 5000);
  }
  

}
