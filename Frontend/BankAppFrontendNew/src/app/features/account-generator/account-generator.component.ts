import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient} from '@angular/common/http';
import { Router} from '@angular/router';
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
    accountType: '',
    balance: '',
  };

  errorMessage ='';
  successMessage = '';

  constructor(private http: HttpClient, private router: Router, private accountService: AccountService) {}



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
  //   const accountToSubmit = {
  //   accountType: this.account.accountType,
  //   balance: this.account.balance
  // };
  this.http.post('http://localhost:8080/api/users/account/createAccount', this.account, { responseType: 'text' })
    .subscribe({
      next: () => {
        //this.accountService.setAccountData(this.account); // speichert temporär im Service
      //this.successMessage ='✅ Account created succesfully';
      setTimeout(() => {this.router.navigate(['/home'], {queryParams: {accountCreationSuccess: true}});}, 1000);
      },
      error: (error) => {
        if (error === 403 ) {
          this.errorMessage = '❌ Account could not have been created, please try again';
          setTimeout(() => {this.router.navigate(['/home'], {queryParams: {accountCreationFailure: true}});}, 1000);
        } else if (error === 0) {
          //this.errorMessage = '❌ Keine Verbindung zum Server';
          setTimeout(() => {this.router.navigate(['/login'], {queryParams: {serverError: true}});}, 1000);
        } else {
          //this.errorMessage = `❌ Fehler vom Server (${error.status}): ${error.statusText || 'Unknown Error'}`;
          setTimeout(() => {this.router.navigate(['/login'], {queryParams: {serverError: true}});}, 1000);
        }



      }
      
    });
}

  
}

