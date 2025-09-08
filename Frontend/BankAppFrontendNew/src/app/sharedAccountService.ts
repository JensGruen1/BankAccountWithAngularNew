import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private accountData$ = new BehaviorSubject<any>(null);

  setAccountData(data: any) {
    this.accountData$.next(data);
  }

  getAccountData() {
    return this.accountData$.asObservable();
  }
}
