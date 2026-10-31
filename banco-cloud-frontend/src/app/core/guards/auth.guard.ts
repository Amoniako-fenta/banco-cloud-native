import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

/**
 * Protege rutas que requieren sesion iniciada. Si la ruta define
 * `data: { roles: ['ADMIN'] }`, ademas exige que el usuario tenga ese rol
 * de realm en Keycloak.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard extends KeycloakAuthGuard {
  constructor(protected override readonly router: Router, protected readonly keycloak: KeycloakService) {
    super(router, keycloak);
  }

  public async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    if (!this.authenticated) {
      await this.keycloak.login({ redirectUri: window.location.origin + state.url });
      return false;
    }

    const rolesRequeridos = route.data['roles'] as string[] | undefined;

    if (!rolesRequeridos || rolesRequeridos.length === 0) {
      return true;
    }

    return rolesRequeridos.every((rol) => this.roles.includes(rol));
  }
}
