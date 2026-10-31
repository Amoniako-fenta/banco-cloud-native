package cl.bancocloud.bff.client;

import cl.bancocloud.bff.model.Cajero;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class CajeroClient {

    private final RestClient restClient;

    public CajeroClient(@Qualifier("cajerosRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Cajero> obtenerCajeros(String comuna, String region, Long sucursalId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/cajeros")
                        .queryParamIfPresent("comuna", Optional.ofNullable(comuna))
                        .queryParamIfPresent("region", Optional.ofNullable(region))
                        .queryParamIfPresent("sucursalId", Optional.ofNullable(sucursalId))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Cajero>>() {});
    }

    public Cajero obtenerPorId(Long id) {
        return restClient.get()
                .uri("/api/cajeros/{id}", id)
                .retrieve()
                .body(Cajero.class);
    }

    public Cajero crear(Cajero cajero) {
        return restClient.post()
                .uri("/api/cajeros")
                .body(cajero)
                .retrieve()
                .body(Cajero.class);
    }

    public Cajero actualizar(Long id, Cajero cajero) {
        return restClient.put()
                .uri("/api/cajeros/{id}", id)
                .body(cajero)
                .retrieve()
                .body(Cajero.class);
    }

    public void desactivar(Long id) {
        restClient.delete()
                .uri("/api/cajeros/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
