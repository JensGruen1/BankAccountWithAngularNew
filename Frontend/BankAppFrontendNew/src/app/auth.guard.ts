import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    // Prüfe, ob User eingeloggt ist
    // Beispiel: Token oder User-Info im localStorage speichern
    const isLoggedIn = !!localStorage.getItem('user'); // oder 'token'

    if (!isLoggedIn) {
      // Wenn nicht eingeloggt, weiterleiten zum Login
      this.router.navigate(['/login']);
      return false;
    }

    return true; // Zugriff erlaubt
  }
}