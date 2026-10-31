import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Sucursal } from '../../../core/models/sucursal.model';
import { SucursalService } from '../../../core/services/sucursal.service';

@Component({
  selector: 'app-sucursales-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sucursales-list.component.html',
  styleUrl: './sucursales-list.component.scss'
})
export class SucursalesListComponent implements OnInit {
  sucursales: Sucursal[] = [];
  cargando = false;
  error = '';
  filtroComuna = '';
  filtroRegion = '';

  constructor(private sucursalService: SucursalService) {}

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.cargando = true;
    this.error = '';
    this.sucursalService.obtenerSucursales(this.filtroComuna || undefined, this.filtroRegion || undefined)
      .subscribe({
        next: (data) => { this.sucursales = data; this.cargando = false; },
        error: () => { this.error = 'No se pudo cargar el listado de sucursales.'; this.cargando = false; }
      });
  }
}
