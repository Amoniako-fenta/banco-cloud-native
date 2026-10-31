package cl.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record QueueRequest(

        @NotBlank(
                message =
                        "El nombre de la cola es obligatorio"
        )

        @Pattern(
                regexp =
                        "^[a-zA-Z0-9._-]+$",

                message =
                        "El nombre de la cola contiene caracteres inválidos"
        )

        String nombre

) {
}