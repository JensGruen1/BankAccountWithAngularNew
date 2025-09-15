import { Component, inject, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { ActivatedRoute, ParamMap } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

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
export class HomeComponent{

accountCreationFailure: boolean = false;
serverConnectionFailed: boolean = false;
successMessage: string = '';
errorMessage: string = '';
serverError: boolean = false;
private route =   inject(ActivatedRoute);
constructor() {
      this.route.queryParamMap
    .pipe(takeUntilDestroyed())
    .subscribe((params: ParamMap) => {
      if (params.has('accountCreationSuccess')) {
        this.successMessage ='✅ Account created succesfully';
      } else if (params.has('accountCreationFailure')){
        this.accountCreationFailure = true;
      } else if (params.has('depositSuccess')) {
        this.successMessage = '✅ Deposit successful'
      }  else if (params.has('transferSuccess')) {
        this.successMessage = '✅ Transfer successful'
      } 


      this.clearMessagesAfterDelay();
    })
}


  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 10000);
  }

}

