import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import { AccountService } from '../../sharedAccountService';

@Component({
  selector: 'app-account-generator',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './account-generator.component.html',
  styleUrl: './account-generator.component.css'
})

export class AccountGeneratorComponent {
    account = {
    accountId: '',
    accountType: '',
    balance: '',
  };

  


 errorMessage ='';
 successMessage = '';

 constructor(private http: HttpClient, private router: Router, private accountService: AccountService,) {}


  //for jwt removed: withCredentials
  // onSubmit() {
  //   this.http.post('http://localhost:8080/api/users/account/createAccount', this.account, {responseType: 'text' }).subscribe({
  //     next: (response) => {
  //       this.successMessage = response;

  //        localStorage.setItem('accountData', JSON.stringify({
  //         accountType: this.account.accountType,
  //         balance: this.account.balance,
  //         accountId: this.account.accountId,
  //       }));  

  //       setTimeout(() => {
  //         this.router.navigate(['/home']);
  //       }, 1000);
  
  //    },
  //   });
  // }



onSubmit() {
    const accountToSubmit = {
    accountType: this.account.accountType,
    //accountId: this.account.accountId,
    balance: this.account.balance
  };
  this.http.post('http://localhost:8080/api/users/account/createAccount', accountToSubmit, { responseType: 'text' })
    .subscribe({
      next: (response) => {
        this.accountService.setAccountData(this.account); // speichert temporär im Service
           setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
      }
    });
}


  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 10000);
  }
  
}

