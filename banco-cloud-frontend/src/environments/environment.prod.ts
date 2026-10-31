export const environment = {
  production: true,
  // En build de produccion esto se reemplaza por la URL publica del BFF
  // (dominio/EC2 detras del proxy). Ver Guia_Despliegue_Banco_Cloud_Keycloak.
  apiUrl: 'https://TU_DOMINIO_O_IP/api',
  keycloak: {
    url: 'https://TU_DOMINIO_O_IP/auth',
    realm: 'banco-cloud',
    clientId: 'banco-cloud-frontend'
  }
};
