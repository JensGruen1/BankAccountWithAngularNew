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
  selector: 'app-home',
  standalone: true, 
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent{}
