import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { HttpClient } from '@angular/common/http';

interface Account {
  accountNumber: string;
  accountType: string;
  balance: number;
  transfers?: any[];
  [key: string]: any;
}

interface Transfer {
  accountNumber: string;
  transferredMoney: number;
  accountNumberReceiver: string;
  accountInfo: string;
  [key: string]: any;
}

interface DateTransferMap {
  [date: string]: Transfer[];
}

@Component({
  selector: 'app-show-transfers',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './show-transfers.component.html',
  styleUrl: './show-transfers.component.css'
})



export class ShowTransfersComponent {

  @Input() accountNumber!: string;
  showAccountDetails: boolean = false;
  account: Account = {
    accountNumber: '',
    accountType: '',
    balance: 0
  };

  userName: string | null = null;

ngOnInit() {
  console.log("HomeComponent geladen!");
  const usernameJson = localStorage.getItem('user');
  if (usernameJson) {
    this.userName = JSON.parse(usernameJson);
  }
}

  //transfersByDate: DateTransferMap = {};
  transfersByDate: { [date: string]: Transfer[] } = {};


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['accountNumber'] && !changes['accountNumber'].firstChange) {
      this.onAccountChange(this.accountNumber);
      this.showAccountDetails = true;
    }
  }

  constructor(private http:HttpClient) {}

  
  onAccountChange(newAccountNumber: string) {
    console.log('Neue Kontonummer:', newAccountNumber);
    this.http.get<Account>(`http://localhost:8080/api/users/getAccountDetails/${newAccountNumber}`, {withCredentials: true}).subscribe({
      next: (data) => {
      this.account = data;
      console.log('Account:', this.account);
    }
    });
    this.http.get<DateTransferMap>(`http://localhost:8080/api/users/getDateTransferMap/${newAccountNumber}`,{withCredentials: true}).subscribe({
     next: (data) => {
     this.transfersByDate = data;
      console.log('DateTransferMap:', this.transfersByDate);
    }
   });
 


  }

  

  getDates(): string[] {
  return Object.keys(this.transfersByDate);
}

}
