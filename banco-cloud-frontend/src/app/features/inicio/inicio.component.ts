import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [],
  templateUrl: './inicio.component.html',
  styles: [`
    .hero { padding: 3rem 1.5rem; text-align: center; }
    h1 { color: #10233f; }
    button { background: #2f6fed; color: #fff; border: none; padding: .6rem 1.4rem; border-radius: .4rem; cursor: pointer; font-size: 1rem; }
  `]
})
export class InicioComponent {
  constructor(private keycloak: KeycloakService) {}

  async estaLogueado(): Promise<boolean> {
    return this.keycloak.isLoggedIn();
  }

  login(): void {
    this.keycloak.login();
  }
}
