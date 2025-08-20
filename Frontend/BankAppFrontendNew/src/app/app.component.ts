import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InactivityService } from './app.inactivityService';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    private inactivityService = inject(InactivityService);

  constructor() {
  }
}
