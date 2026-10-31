import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './app.component.html',
  styles: [`
    body { margin: 0; font-family: 'Segoe UI', Roboto, sans-serif; }
  `]
})
export class AppComponent {
  title = 'banco-cloud-frontend';
}
