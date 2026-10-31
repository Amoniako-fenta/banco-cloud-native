export interface Cajero {
  id?: number;
  nombre: string;
  direccion: string;
  comuna: string;
  region: string;
  horarioApertura: string;
  horarioCierre: string;
  activo?: boolean;
  sucursalId: number;
}
