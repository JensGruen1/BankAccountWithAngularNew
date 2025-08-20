import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 

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
account = {
  accountNumber: '',
  balance: '',
  accountType: '',
};


//List of Accounts
//accounts: typeof this.account[] = [];
accounts: { accountNumber: number; accountType: string; balance: number }[] = []; 

selectedAccount: Account | null = null;
depositAmount: number = 0;


constructor(private http:HttpClient, private router:Router) {}


//maybe only the accountNumbers
ngOnInit (): void {
  this.http.get<{ accountNumber: number; accountType: string; balance: number }[]>('http://localhost:8080/api/users/showAccounts',{ withCredentials: true }).subscribe({
    next: (data) => {
      console.log('Accounts vom Backend:', data);
      this.accounts = data;
    }
  });
}

/* onAccountSelect (event:Event): void {
  const selectedAccount = (event.target as HTMLSelectElement).value;
  localStorage.setItem('activeAccount', selectedAccount);
  console.log('Gespeicherter Account:', selectedAccount);
} */

setAccountToLocalStorage(account: Account):void {
  localStorage.setItem('activeAccount', JSON.stringify(account));
}




onSubmit () {
  const raw = localStorage.getItem('activeAccount');
   if (!raw) {
    console.error('Kein aktiver Account gefunden!');
    return;
  }

  const activeAccount: Account = JSON.parse(raw);

  const newBalance = activeAccount.balance + this.depositAmount;

  const updatedAccount: Account = {
      accountNumber: activeAccount.accountNumber,
      accountType: activeAccount.accountType,
      balance: newBalance
  };

  const deposit = {
  accountNumber: activeAccount.accountNumber,
  amount: this.depositAmount   // 👈 gleiches Feld wie im Backend
};



  localStorage.setItem('deposit', JSON.stringify(deposit));


  this.http.post('http://localhost:8080/api/users/deposit',deposit, { withCredentials: true, responseType: 'text'}).subscribe({
        next: (response) => {
          console.log('Deposit erfolgreich:', response);
          setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);

        },
        
        error: (err) => console.error('Fehler beim Deposit:', err)

  });
}



}
