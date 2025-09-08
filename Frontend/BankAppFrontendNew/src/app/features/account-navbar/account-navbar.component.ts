import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
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

@Component({
  selector: 'app-account-navbar',
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './account-navbar.component.html',
  styleUrl: './account-navbar.component.css'
})
export class AccountNavbarComponent implements OnInit{


@Output() accountChanged = new EventEmitter<string>();
//accounts: Account[] = [];
//selectedAccount: Account | null = null;
listOfAccountNumbers: string[] = [];

constructor(private http: HttpClient, private router: Router, private authService: AuthService) {}

 onAccountChange(event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    this.accountChanged.emit(value);
  }


  //with jwt removed: withCredentials
ngOnInit (): void {
  this.http.get<string[]>('http://localhost:8080/api/users/listOfAccountNumbers').subscribe({
    next: (data) => {
      this.listOfAccountNumbers = data;
    }
  });
}

//  setAccountToLocalStorage(accountNumber: string):void {
//  localStorage.setItem('activeAccount', JSON.stringify(accountNumber));
//}


}
