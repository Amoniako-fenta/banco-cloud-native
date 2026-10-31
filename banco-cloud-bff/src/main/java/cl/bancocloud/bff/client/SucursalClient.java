package cl.bancocloud.bff.client;

import cl.bancocloud.bff.model.Sucursal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class SucursalClient {

    private final RestClient restClient;

    public SucursalClient(@Qualifier("sucursalesRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Sucursal> obtenerSucursales(String comuna, String region) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/sucursales")
                        .queryParamIfPresent("comuna", Optional.ofNullable(comuna))
                        .queryParamIfPresent("region", Optional.ofNullable(region))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Sucursal>>() {});
    }

    public Sucursal obtenerPorId(Long id) {
        return restClient.get()
                .uri("/api/sucursales/{id}", id)
                .retrieve()
                .body(Sucursal.class);
    }

    public Sucursal crear(Sucursal sucursal) {
        return restClient.post()
                .uri("/api/sucursales")
                .body(sucursal)
                .retrieve()
                .body(Sucursal.class);
    }

    public Sucursal actualizar(Long id, Sucursal sucursal) {
        return restClient.put()
                .uri("/api/sucursales/{id}", id)
                .body(sucursal)
                .retrieve()
                .body(Sucursal.class);
    }

    public void desactivar(Long id) {
        restClient.delete()
                .uri("/api/sucursales/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
