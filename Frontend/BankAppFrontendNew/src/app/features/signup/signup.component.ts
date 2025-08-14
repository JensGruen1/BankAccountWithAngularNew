import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { HttpClient } from '@angular/common/http';
import {Router} from '@angular/router';



@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,

  ],
  templateUrl: `./signup.component.html`,
  styleUrls:[ './signup.component.css']
})
export class SignupComponent {
    user = {
    username: '',
    password: '',
  };

 errorMessage ='';
 successMessage = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    this.http.post('http://localhost:8080/api/users/signup', this.user, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.successMessage = response;
        this.errorMessage ='';
        this.user = { username: '', password: '' }; // Felder leeren
        this.clearMessagesAfterDelay();
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
  
      },
      error: (error) => {
        if (error.status === 409) {
          this.errorMessage = 'User already exists';
        } else if (error.status === 400) {
        this.errorMessage = 'Invalid input';
        } else {
        this.errorMessage = 'Unexpected error: ' + error.message;
        }
        this.successMessage = '';
        this.clearMessagesAfterDelay();
      },
    });
  }

    private clearMessagesAfterDelay() {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 5000); // 5 Sekunden
  }

}
