import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario } from '../models/usuario.model';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly baseUrl = `${environment.apiUrl}/usuarios`;

  constructor(private http: HttpClient) {}

  /** Info del usuario autenticado segun el JWT (util para saber si ya esta registrado). */
  obtenerMiPerfil(): Observable<Usuario | { email: string; registrado: false }> {
    return this.http.get<Usuario | { email: string; registrado: false }>(`${this.baseUrl}/me`);
  }

  /** Autoregistro tras el primer login. */
  registrar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.baseUrl, usuario);
  }

  /** Solo ADMIN: lista usuarios, opcionalmente filtrados por autorizado. */
  obtenerUsuarios(autorizado?: boolean): Observable<Usuario[]> {
    let params: any = {};
    if (autorizado !== undefined) {
      params.autorizado = autorizado;
    }
    return this.http.get<Usuario[]>(this.baseUrl, { params });
  }

  obtenerPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
  }

  actualizar(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}`, usuario);
  }

  /** Solo ADMIN: autoriza a un usuario pendiente. */
  autorizar(id: number): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}/autorizar`, {});
  }

  desactivar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
