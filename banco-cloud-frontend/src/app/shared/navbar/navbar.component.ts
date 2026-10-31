import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styles: [`
    nav { display: flex; align-items: center; gap: 1.25rem; padding: .75rem 1.5rem; background: #10233f; color: #fff; }
    nav a { color: #cfe0ff; text-decoration: none; font-size: .95rem; }
    nav a.active, nav a:hover { color: #fff; font-weight: 600; }
    .spacer { flex: 1; }
    .brand { font-weight: 700; font-size: 1.1rem; margin-right: .5rem; }
    button { background: #2f6fed; color: #fff; border: none; padding: .4rem .9rem; border-radius: .4rem; cursor: pointer; }
    button:hover { background: #1d54c9; }
  `]
})
export class NavbarComponent implements OnInit {
  autenticado = false;
  esAdmin = false;
  nombreUsuario = '';

  constructor(private keycloak: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    this.autenticado = await this.keycloak.isLoggedIn();
    if (this.autenticado) {
      const perfil = await this.keycloak.loadUserProfile();
      this.nombreUsuario = perfil.username ?? perfil.email ?? '';
      const roles = this.keycloak.getUserRoles();
      this.esAdmin = roles.includes('ADMIN');
    }
  }

  login(): void {
    this.keycloak.login();
  }

  logout(): void {
    this.keycloak.logout(window.location.origin);
  }
}
