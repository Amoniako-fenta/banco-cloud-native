package cl.bancocloud.sucursales.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitTestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.sucursal.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.sucursal.routing-key}")
    private String routingKey;

    public RabbitTestProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarErrorDePrueba() {

        String mensaje = """
                {
                    "sucursalId": 999,
                    "nombre": "Prueba DLQ",
                    "comuna": "Prueba",
                    "region": "Prueba",
                    "evento": "FORZAR_ERROR"
                }
                """;

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                mensaje
        );
    }
}