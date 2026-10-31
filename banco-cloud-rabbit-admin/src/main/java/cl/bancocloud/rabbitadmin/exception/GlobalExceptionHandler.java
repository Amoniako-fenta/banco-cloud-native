package cl.bancocloud.rabbitadmin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, Object>>
    manejarValidacion(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errores =
                new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(
                        error -> errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> respuesta =
                new LinkedHashMap<>();

        respuesta.put(
                "status",
                400
        );

        respuesta.put(
                "error",
                "Datos inválidos"
        );

        respuesta.put(
                "detalles",
                errores
        );

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(respuesta);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    manejarErrorGeneral(
            Exception ex
    ) {

        ex.printStackTrace();

        Map<String, Object> respuesta =
                new LinkedHashMap<>();

        respuesta.put(
                "status",
                500
        );

        respuesta.put(
                "error",
                "Error interno"
        );

        respuesta.put(
                "mensaje",
                ex.getMessage()
        );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(respuesta);
    }
}