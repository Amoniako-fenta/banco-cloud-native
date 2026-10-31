import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';

@Component({
  selector: 'app-mi-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mi-perfil.component.html',
  styleUrl: './mi-perfil.component.scss'
})
export class MiPerfilComponent implements OnInit {
  cargando = true;
  registrado = false;
  usuario: Usuario | null = null;
  error = '';
  registrando = false;

  constructor(private usuarioService: UsuarioService, private keycloak: KeycloakService) {}

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.cargando = true;
    this.usuarioService.obtenerMiPerfil().subscribe({
      next: (data: any) => {
        if (data && data.registrado === false) {
          this.registrado = false;
        } else {
          this.registrado = true;
          this.usuario = data as Usuario;
        }
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo obtener tu perfil desde el BFF.';
        this.cargando = false;
      }
    });
  }

  async registrarme(): Promise<void> {
    this.registrando = true;
    const perfilKeycloak = await this.keycloak.loadUserProfile();

    const nuevoUsuario: Usuario = {
      nombre: `${perfilKeycloak.firstName ?? ''} ${perfilKeycloak.lastName ?? ''}`.trim() || (perfilKeycloak.username ?? ''),
      email: perfilKeycloak.email ?? ''
    };

    this.usuarioService.registrar(nuevoUsuario).subscribe({
      next: () => { this.registrando = false; this.cargarPerfil(); },
      error: () => { this.error = 'No se pudo completar el registro.'; this.registrando = false; }
    });
  }
}
