import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cajero } from '../../../core/models/cajero.model';
import { CajeroService } from '../../../core/services/cajero.service';

@Component({
  selector: 'app-cajeros-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cajeros-list.component.html',
  styleUrl: './cajeros-list.component.scss'
})
export class CajerosListComponent implements OnInit {
  cajeros: Cajero[] = [];
  cargando = false;
  error = '';
  filtroComuna = '';
  filtroRegion = '';

  constructor(private cajeroService: CajeroService) {}

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.cargando = true;
    this.error = '';
    this.cajeroService.obtenerCajeros(this.filtroComuna || undefined, this.filtroRegion || undefined)
      .subscribe({
        next: (data) => { this.cajeros = data; this.cargando = false; },
        error: () => { this.error = 'No se pudo cargar el listado de cajeros.'; this.cargando = false; }
      });
  }
}
