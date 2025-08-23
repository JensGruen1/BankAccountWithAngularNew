import { Injectable, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { fromEvent, merge, Observable, Subject, timer } from 'rxjs';
import { filter, startWith, switchMap, takeUntil, tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class InactivityService {
  private router = inject(Router);
  private authService = inject(AuthService);

  private inactivityTime = 180 * 1000; // 1 Minute

  private protectedRoute = true; // Flag, ob Timeout aktiv ist
  private resetTimer$ = new Subject<void>();

  constructor() {
    this.initRouteListener();
    this.initActivityListener();
  }

    private initRouteListener() {
    // Prüfe bei jedem NavigationEnd, ob Timeout aktiviert werden soll
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      const url = event.urlAfterRedirects;
      // Timeout nur aktiv auf geschützten Seiten
      this.protectedRoute = !url.startsWith('/login') && !url.startsWith('/signup');
      this.resetTimer$.next();
    });
  }

  private initActivityListener() {
    const activityEvents$: Observable<Event | null> = merge(
      fromEvent(document, 'mousemove'),
      fromEvent(document, 'mousedown'),
      fromEvent(document, 'keydown'),
      fromEvent(document, 'touchstart'),
      fromEvent(document, 'scroll')
    ).pipe(startWith(null));

    activityEvents$
      .pipe(
        filter(() => this.protectedRoute), // nur wenn Timeout aktiv
        switchMap(() => timer(this.inactivityTime).pipe(
          // Reset, wenn Route wechselt oder neue Aktivität
          takeUntil(this.resetTimer$)
        )),
        tap(() => this.logout())
      )
      .subscribe();
  }

  private logout() {
    this.authService.logoutInactivity();
    alert('Du wurdest aufgrund von Inaktivität ausgeloggt.');
  }
}