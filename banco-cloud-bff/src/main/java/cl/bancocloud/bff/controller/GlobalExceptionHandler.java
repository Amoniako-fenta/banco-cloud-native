package cl.bancocloud.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

/**
 * Traduce los errores que devuelven los microservicios internos
 * (usuarios, sucursales, cajeros) al código HTTP correspondiente,
 * en vez de que el BFF los convierta todos en un 500 genérico.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorDownstream(RestClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of(
                        "error", "Error al comunicarse con un servicio interno",
                        "detalle", ex.getStatusText()
                ));
    }
}
