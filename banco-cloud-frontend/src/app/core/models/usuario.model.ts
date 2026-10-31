export interface Usuario {
  id?: number;
  nombre: string;
  email: string;
  activo?: boolean;
  autorizado?: boolean;
  rol?: string;
}
