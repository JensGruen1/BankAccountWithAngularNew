import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'; 
import { AuthService } from '../../auth.service';

interface Transfer {
  accountNumber: string;
  transferredMoney: number;
  accountNumberReceiver: string;
}

interface Account {
  accountNumber: string;
  accountType: string;
  balance: number;
}


@Component({
  selector: 'app-transfer',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent implements OnInit{

    accounts: Account[] = [];
    transferredMoney:number = 0;
    accountNumberReceiver: string ='';
    selectedAccount: Account | null = null;
  

    constructor(private http:HttpClient, private router:Router, private authService: AuthService) {}

    ngOnInit():void {
    this.http.get<Account[]>('http://localhost:8080/api/users/showAccounts').subscribe({
      next: (data) => {
      this.accounts = data;
    }

    });
  }

    setAccountToLocalStorage(account: Account):void {
      localStorage.setItem('activeAccount', JSON.stringify(account));
    }


  onSubmit() {
  const raw = localStorage.getItem('activeAccount');
   if (!raw) {
    console.error('Kein aktiver Account gefunden!');
    return;
  }
  const activeAccount: Account = JSON.parse(raw);

  const transfer = {
  accountNumber: activeAccount.accountNumber,
  transferredMoney: this.transferredMoney,
  accountNumberReceiver: this.accountNumberReceiver  
};


    this.http.post('http://localhost:8080/api/users/transfer',transfer, {responseType: 'text'}).subscribe({
        next: (response) => {

          localStorage.removeItem('activeAccount');
          setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
      },
        error: (err: HttpErrorResponse) => {
          const body = (typeof err.error === 'string') ? err.error : '';


          console.error('Fehler beim Transfer:', err);
          if (body.includes('Account does not exist')) {
            alert('Der Account existiert nicht');
          } else {
            alert(body || `Fehler ${err.status}: ${err.statusText || 'Unbekannt'}`);
          }
        }
    });
  }

}
