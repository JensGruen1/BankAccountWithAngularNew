import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 
import { AuthService } from '../../auth.service';


interface Account {
  accountNumber: string;
  accountType: string;
  balance: number;
}

interface Withdraw {
  accountNumber: string;
  withdrawAmount: number;
}


@Component({
  selector: 'app-withdraw',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './withdraw.component.html',
  styleUrl: './withdraw.component.css'
})
export class WithdrawComponent implements OnInit {

  accounts: Account[] = [];
  withdrawAmount:number = 0;
  selectedAccount: Account | null = null;


  //constructor(private http:HttpClient, router:Router, private authService: AuthService) {}
    constructor(private http:HttpClient, private router:Router, private authService: AuthService) {}

  ngOnInit():void {
    //this.authService.checkUser();
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

  const withdraw = {
  accountNumber: activeAccount.accountNumber,
  amount: this.withdrawAmount   
};

    this.http.post('http://localhost:8080/api/users/withdraw',withdraw, {responseType: 'text'}).subscribe({
        next: (response) => {
          localStorage.removeItem('deposit');
          localStorage.removeItem('activeAccount');
          setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
      },
        error: (err) => console.error('Fehler beim Withdraw:', err)
    });
  }



}
