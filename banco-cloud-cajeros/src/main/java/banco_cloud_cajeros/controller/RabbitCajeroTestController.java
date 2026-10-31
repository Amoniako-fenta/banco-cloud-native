package banco_cloud_cajeros.controller;

import banco_cloud_cajeros.rabbit.producer.RabbitCajeroTestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit-test")
public class RabbitCajeroTestController {

    private final RabbitCajeroTestProducer rabbitCajeroTestProducer;


    public RabbitCajeroTestController(
            RabbitCajeroTestProducer rabbitCajeroTestProducer
    ) {

        this.rabbitCajeroTestProducer =
                rabbitCajeroTestProducer;
    }


    @PostMapping("/error")
    public ResponseEntity<String> enviarError() {

        rabbitCajeroTestProducer
                .enviarErrorDePrueba();


        return ResponseEntity.ok(
                "Mensaje de prueba de Cajeros enviado. Revisar DLQ."
        );
    }
}