export interface Sucursal {
  id?: number;
  nombre: string;
  direccion: string;
  comuna: string;
  region: string;
  horarioApertura: string;
  horarioCierre: string;
  activa?: boolean;
}
