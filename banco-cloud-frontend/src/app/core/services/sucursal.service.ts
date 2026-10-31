import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Sucursal } from '../models/sucursal.model';

@Injectable({ providedIn: 'root' })
export class SucursalService {
  private readonly baseUrl = `${environment.apiUrl}/sucursales`;

  constructor(private http: HttpClient) {}

  obtenerSucursales(comuna?: string, region?: string): Observable<Sucursal[]> {
    let params: any = {};
    if (comuna) params.comuna = comuna;
    if (region) params.region = region;
    return this.http.get<Sucursal[]>(this.baseUrl, { params });
  }

  obtenerPorId(id: number): Observable<Sucursal> {
    return this.http.get<Sucursal>(`${this.baseUrl}/${id}`);
  }

  crear(sucursal: Sucursal): Observable<Sucursal> {
    return this.http.post<Sucursal>(this.baseUrl, sucursal);
  }

  actualizar(id: number, sucursal: Sucursal): Observable<Sucursal> {
    return this.http.put<Sucursal>(`${this.baseUrl}/${id}`, sucursal);
  }

  desactivar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
