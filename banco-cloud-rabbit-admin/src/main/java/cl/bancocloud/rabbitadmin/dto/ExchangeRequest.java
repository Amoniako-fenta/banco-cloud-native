package cl.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExchangeRequest(

        @NotBlank(
                message =
                        "El nombre del exchange es obligatorio"
        )

        @Pattern(
                regexp =
                        "^[a-zA-Z0-9._-]+$",

                message =
                        "El nombre del exchange contiene caracteres inválidos"
        )

        String nombre

) {
}