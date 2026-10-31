import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../core/services/usuario.service';
import { Usuario } from '../../../core/models/usuario.model';

@Component({
  selector: 'app-usuarios-pendientes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuarios-pendientes.component.html',
  styleUrl: './usuarios-pendientes.component.scss'
})
export class UsuariosPendientesComponent implements OnInit {
  usuarios: Usuario[] = [];
  cargando = false;
  error = '';
  procesandoId: number | null = null;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    // autorizado=false -> pendientes de aprobación
    this.usuarioService.obtenerUsuarios(false).subscribe({
      next: (data) => { this.usuarios = data; this.cargando = false; },
      error: () => { this.error = 'No se pudo cargar la lista (¿tienes rol ADMIN en Keycloak?).'; this.cargando = false; }
    });
  }

  autorizar(usuario: Usuario): void {
    if (!usuario.id) return;
    this.procesandoId = usuario.id;
    this.usuarioService.autorizar(usuario.id).subscribe({
      next: () => { this.procesandoId = null; this.cargar(); },
      error: () => { this.error = 'No se pudo autorizar al usuario.'; this.procesandoId = null; }
    });
  }
}
