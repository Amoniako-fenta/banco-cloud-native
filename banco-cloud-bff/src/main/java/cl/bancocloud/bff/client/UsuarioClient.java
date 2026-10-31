package cl.bancocloud.bff.client;

import cl.bancocloud.bff.model.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(@Qualifier("usuariosRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Usuario> obtenerUsuarios(Boolean autorizado) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/usuarios")
                        .queryParamIfPresent("autorizado", Optional.ofNullable(autorizado))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Usuario>>() {});
    }

    public Usuario obtenerPorId(Long id) {
        return restClient.get()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .body(Usuario.class);
    }

    public Usuario obtenerPorEmail(String email) {
        return restClient.get()
                .uri("/api/usuarios/email/{email}", email)
                .retrieve()
                .body(Usuario.class);
    }

    public Usuario registrar(Usuario usuario) {
        return restClient.post()
                .uri("/api/usuarios")
                .body(usuario)
                .retrieve()
                .body(Usuario.class);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        return restClient.put()
                .uri("/api/usuarios/{id}", id)
                .body(usuario)
                .retrieve()
                .body(Usuario.class);
    }

    public Usuario autorizar(Long id) {
        return restClient.put()
                .uri("/api/usuarios/{id}/autorizar", id)
                .retrieve()
                .body(Usuario.class);
    }

    public void desactivar(Long id) {
        restClient.delete()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
