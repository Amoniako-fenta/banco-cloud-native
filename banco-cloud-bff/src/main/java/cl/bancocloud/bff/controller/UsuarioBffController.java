package cl.bancocloud.bff.controller;

import cl.bancocloud.bff.client.UsuarioClient;
import cl.bancocloud.bff.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioBffController {

    private final UsuarioClient usuarioClient;

    public UsuarioBffController(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    /**
     * Devuelve la info del usuario autenticado según el JWT.
     * Sirve para probar rápido que el BFF valida bien el token:
     * curl sin token -> 401. curl con token -> 200.
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            email = jwt.getClaimAsString("preferred_username");
        }

        try {
            Usuario usuario = usuarioClient.obtenerPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                Map<String, Object> body = new HashMap<>();
                body.put("email", email);
                body.put("registrado", false);
                return ResponseEntity.ok(body);
            }
            throw ex;
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> obtenerUsuarios(
            @RequestParam(required = false) Boolean autorizado) {

        return ResponseEntity.ok(usuarioClient.obtenerUsuarios(autorizado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioClient.obtenerPorId(id));
    }

    /**
     * Autoregistro: cualquier usuario autenticado (recién logueado por
     * primera vez vía Keycloak) puede crear su ficha en banco-cloud-usuarios.
     */
    @PostMapping
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioClient.registrar(usuario));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioClient.actualizar(id, usuario));
    }

    @PutMapping("/{id}/autorizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> autorizar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioClient.autorizar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioClient.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
