import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Cajero } from '../models/cajero.model';

@Injectable({ providedIn: 'root' })
export class CajeroService {
  private readonly baseUrl = `${environment.apiUrl}/cajeros`;

  constructor(private http: HttpClient) {}

  obtenerCajeros(comuna?: string, region?: string, sucursalId?: number): Observable<Cajero[]> {
    let params: any = {};
    if (comuna) params.comuna = comuna;
    if (region) params.region = region;
    if (sucursalId) params.sucursalId = sucursalId;
    return this.http.get<Cajero[]>(this.baseUrl, { params });
  }

  obtenerPorId(id: number): Observable<Cajero> {
    return this.http.get<Cajero>(`${this.baseUrl}/${id}`);
  }

  crear(cajero: Cajero): Observable<Cajero> {
    return this.http.post<Cajero>(this.baseUrl, cajero);
  }

  actualizar(id: number, cajero: Cajero): Observable<Cajero> {
    return this.http.put<Cajero>(`${this.baseUrl}/${id}`, cajero);
  }

  desactivar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
