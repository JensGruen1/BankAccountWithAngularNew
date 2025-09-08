import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


interface LoginResponse {
  token: string;
}


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
 logoutSuccess = false;
 timeoutLogout = false;

private route = inject(ActivatedRoute);


constructor(private http: HttpClient, private router: Router) {
    this.route.queryParamMap
      .pipe(takeUntilDestroyed())
      .subscribe((params: ParamMap) => {      
          if (params.has('logout')) {
          this.logoutSuccess = true;
          setTimeout(() => this.logoutSuccess = false, 3000);
        } else {
          this.logoutSuccess = false;
        }
      });
}

  ngOnInit() {
    localStorage.clear();
  }

//without jwt:
  // onSubmit() {
  //   this.http.post('http://localhost:8080/api/users/login', this.user, { withCredentials: true, responseType: 'text' }).subscribe({
  //     next: (response) => {
  //       this.successMessage = response;
  //       localStorage.setItem('user', JSON.stringify(this.user.username));
  //       this.clearMessagesAfterDelay();
  //       setTimeout(() => {
  //         this.router.navigate(['/home']);
  //       }, 1000);
  
  //     },
  //     error: (error) => {
  //       if (error.status === 0) {
  //         // Status 0 = kein Kontakt zum Backend
  //         this.errorMessage = '❌ Keine Verbindung zum Server';
  //       } else if (error.status === 401) {
  //         // Status 401 = Login falsch
  //         this.errorMessage = '❌ Ungültige Anmeldedaten';
  //       } else {
  //         // Alle anderen HTTP-Fehler
  //         this.errorMessage = `❌ Fehler vom Server (${error.status}): ${error.statusText || 'Unknown Error'}`;
  //       }
  //       this.clearMessagesAfterDelay();
  //     },
  //   });
  // }


    // with jwt
    onSubmit() {
    this.http.post<LoginResponse>('http://localhost:8080/api/users/login', this.user).subscribe({
      next: (response) => {
        localStorage.setItem('jwt_token', response.token);
          this.successMessage = '✅ Login erfolgreich';
          this.clearMessagesAfterDelay();;
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
          this.errorMessage = `❌ Fehler vom Server (${error.status}): ${error.statusText || 'Unknown Error'}`;
        }
        this.clearMessagesAfterDelay();
      },
    });
  }



//only for jwt needed:
    logout() {
    localStorage.removeItem('jwt_token');
    this.router.navigate(['/login'], { queryParams: { logout: 'true' } });
  }



  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 10000);
  }
  
}
