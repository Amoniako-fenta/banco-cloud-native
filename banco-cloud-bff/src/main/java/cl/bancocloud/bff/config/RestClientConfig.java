package cl.bancocloud.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Define un RestClient por cada microservicio interno.
 * Las URL se leen de application.properties (que a su vez las toma de
 * variables de entorno), para que en AWS solo haya que setear
 * USUARIOS_SERVICE_URL / SUCURSALES_SERVICE_URL / CAJEROS_SERVICE_URL
 * en /home/ec2-user/.banco-cloud.env sin tocar código.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient usuariosRestClient(
            @Value("${bancocloud.usuarios.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    public RestClient sucursalesRestClient(
            @Value("${bancocloud.sucursales.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    public RestClient cajerosRestClient(
            @Value("${bancocloud.cajeros.url}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
