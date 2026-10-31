package banco_cloud_cajeros.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitCajeroTestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.cajero.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.cajero.routing-key}")
    private String routingKey;


    public RabbitCajeroTestProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void enviarErrorDePrueba() {

        String mensaje = """
                {
                    "cajeroId": 999,
                    "nombre": "Cajero prueba DLQ",
                    "comuna": "Prueba",
                    "region": "Prueba",
                    "sucursalId": 999,
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