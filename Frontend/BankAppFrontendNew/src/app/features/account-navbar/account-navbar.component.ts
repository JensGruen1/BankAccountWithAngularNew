import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 

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


@Output() outputAccountNumber = new EventEmitter<string>();
listOfAccountNumbers: string[] = [];
accountNumber: string ='';

constructor(private http: HttpClient, private router: Router) {}


   onAccountChange(accountNumber: string) {
     this.outputAccountNumber.emit(accountNumber);
   }



ngOnInit (): void {
  this.http.get<string[]>('http://localhost:8080/api/users/listOfAccountNumbers').subscribe({
    next: (data) => {
      this.listOfAccountNumbers = data;
    }
  });
}



}
