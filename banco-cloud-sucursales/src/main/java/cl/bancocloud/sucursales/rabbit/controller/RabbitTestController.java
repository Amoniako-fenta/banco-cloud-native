package cl.bancocloud.sucursales.rabbit.controller;

import cl.bancocloud.sucursales.rabbit.producer.RabbitTestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit-test")
public class RabbitTestController {

    private final RabbitTestProducer rabbitTestProducer;

    public RabbitTestController(
            RabbitTestProducer rabbitTestProducer
    ) {
        this.rabbitTestProducer = rabbitTestProducer;
    }

    @PostMapping("/error")
    public ResponseEntity<String> enviarError() {

        rabbitTestProducer.enviarErrorDePrueba();

        return ResponseEntity.ok(
                "Mensaje de prueba enviado. Revisar DLQ."
        );
    }
}