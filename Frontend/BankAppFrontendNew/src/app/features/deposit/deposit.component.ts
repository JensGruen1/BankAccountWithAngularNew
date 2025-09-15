import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 
import { BehaviorSubject } from 'rxjs';

interface Account {
  accountNumber: string;
  accountType: string;
  balance: number;
}

interface Deposit {
  accountNumber: string;
  depositAmount: number;
}

@Component({
  selector: 'app-deposit',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './deposit.component.html',
  styleUrl: './deposit.component.css'
})

export class DepositComponent {




//Account to submit
account:Account = {accountNumber:'', accountType: '', balance: 0};

//List of Accounts
accounts: Account[] = []; 

depositAmount: number = 0;
private accountData$ = new BehaviorSubject<any>(null);


constructor(private http:HttpClient, private router:Router) {}


// for jwt withCredentials ist removed!
ngOnInit (): void {
  this.http.get<Account[]>('http://localhost:8080/api/users/showAccounts').subscribe({
    next: (data) => {
      this.accounts = data;
    }
    //error handling still missing
  });
}


setAccountData(account: Account) {
    this.accountData$.next(account);
  }

getAccountData() {
   return this.accountData$.getValue();
}


onSubmit () {

const deposit = {
  accountNumber: this.getAccountData().accountNumber,
  amount:  this.depositAmount
};

  this.http.post('http://localhost:8080/api/users/deposit',deposit, {responseType: 'text'}).subscribe({
        next: () => {
          setTimeout(() => {
          this.router.navigate(['/home'],{queryParams: {depositSuccess: true}});
        }, 1000);
        },
          error: (err) => {
        if (err.status === 401) {
          this.router.navigate(['/login'], {queryParams: {jwtTokenError: true}});
        } else {
          this.router.navigate(['/login'], {queryParams: {serverError: true}});
        }
      }

  });
}



}
