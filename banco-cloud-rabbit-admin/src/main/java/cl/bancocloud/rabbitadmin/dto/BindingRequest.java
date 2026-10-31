package cl.bancocloud.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BindingRequest(

        @NotBlank(
                message =
                        "El nombre de la cola es obligatorio"
        )

        @Pattern(
                regexp =
                        "^[a-zA-Z0-9._-]+$",

                message =
                        "Nombre de cola inválido"
        )

        String queue,


        @NotBlank(
                message =
                        "El nombre del exchange es obligatorio"
        )

        @Pattern(
                regexp =
                        "^[a-zA-Z0-9._-]+$",

                message =
                        "Nombre de exchange inválido"
        )

        String exchange,


        @NotBlank(
                message =
                        "La routing key es obligatoria"
        )

        @Pattern(
                regexp =
                        "^[a-zA-Z0-9._-]+$",

                message =
                        "Routing key inválida"
        )

        String routingKey

) {
}