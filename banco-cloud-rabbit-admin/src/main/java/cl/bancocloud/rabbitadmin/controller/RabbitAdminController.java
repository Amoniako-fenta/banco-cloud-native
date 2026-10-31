package cl.bancocloud.rabbitadmin.controller;

import cl.bancocloud.rabbitadmin.dto.BindingRequest;
import cl.bancocloud.rabbitadmin.dto.ExchangeRequest;
import cl.bancocloud.rabbitadmin.dto.QueueRequest;
import cl.bancocloud.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@RestController
@RequestMapping("/api/rabbit")
public class RabbitAdminController {

    private final RabbitAdminService service;


    public RabbitAdminController(
            RabbitAdminService service
    ) {

        this.service =
                service;
    }


    // ==================================
    // QUEUES
    // ==================================

    @PostMapping("/queues")
    public ResponseEntity<Map<String, String>>
    crearQueue(

            @Valid
            @RequestBody
            QueueRequest request

    ) {

        service.crearQueue(
                request.nombre()
        );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        "Cola creada correctamente",

                        "queue",
                        request.nombre()
                )
        );
    }


    @DeleteMapping("/queues/{name}")
    public ResponseEntity<Map<String, Object>>
    eliminarQueue(

            @PathVariable
            String name

    ) {

        boolean eliminada =
                service.eliminarQueue(
                        name
                );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        eliminada
                                ? "Cola eliminada correctamente"
                                : "La cola no existe",

                        "queue",
                        name,

                        "eliminada",
                        eliminada
                )
        );
    }


    // ==================================
    // EXCHANGES
    // ==================================

    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, String>>
    crearExchange(

            @Valid
            @RequestBody
            ExchangeRequest request

    ) {

        service.crearExchange(
                request.nombre()
        );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        "Exchange creado correctamente",

                        "exchange",
                        request.nombre()
                )
        );
    }


    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<Map<String, Object>>
    eliminarExchange(

            @PathVariable
            String name

    ) {

        boolean eliminado =
                service.eliminarExchange(
                        name
                );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        eliminado
                                ? "Exchange eliminado correctamente"
                                : "El exchange no existe",

                        "exchange",
                        name,

                        "eliminado",
                        eliminado
                )
        );
    }


    // ==================================
    // BINDINGS
    // ==================================

    @PostMapping("/bindings")
    public ResponseEntity<Map<String, String>>
    crearBinding(

            @Valid
            @RequestBody
            BindingRequest request

    ) {

        service.crearBinding(

                request.queue(),

                request.exchange(),

                request.routingKey()
        );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        "Binding creado correctamente",

                        "queue",
                        request.queue(),

                        "exchange",
                        request.exchange(),

                        "routingKey",
                        request.routingKey()
                )
        );
    }


    @DeleteMapping("/bindings")
    public ResponseEntity<Map<String, String>>
    eliminarBinding(

            @Valid
            @RequestBody
            BindingRequest request

    ) {

        service.eliminarBinding(

                request.queue(),

                request.exchange(),

                request.routingKey()
        );


        return ResponseEntity.ok(

                Map.of(
                        "mensaje",
                        "Binding eliminado correctamente",

                        "queue",
                        request.queue(),

                        "exchange",
                        request.exchange(),

                        "routingKey",
                        request.routingKey()
                )
        );
    }
}
