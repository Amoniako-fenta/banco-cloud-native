package banco_cloud_usuarios.controller;

import banco_cloud_usuarios.rabbit.producer.RabbitUsuarioTestProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit-test")
public class RabbitUsuarioTestController {

    private final RabbitUsuarioTestProducer producer;


    public RabbitUsuarioTestController(
            RabbitUsuarioTestProducer producer
    ) {
        this.producer = producer;
    }


    @PostMapping("/error")
    public ResponseEntity<String> probarError() {

        producer.enviarError();

        return ResponseEntity.ok(
                "Mensaje de prueba de Usuarios enviado. Revisar DLQ."
        );
    }


    @PostMapping("/reintento")
    public ResponseEntity<String> probarReintento() {

        producer.enviarReintento();

        return ResponseEntity.ok(
                "Mensaje de reintento de Usuarios enviado."
        );
    }
}