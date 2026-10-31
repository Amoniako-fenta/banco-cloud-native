package banco_cloud_usuarios.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitUsuarioTestProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.usuario.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.usuario.routing-key}")
    private String routingKey;


    public RabbitUsuarioTestProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void enviarError() {

        String mensaje = """
                {
                    "usuarioId": 999,
                    "nombre": "Usuario prueba DLQ",
                    "email": "error@prueba.cl",
                    "autorizado": false,
                    "rol": "FORZAR_ERROR",
                    "evento": "FORZAR_ERROR"
                }
                """;

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                mensaje
        );
    }


    public void enviarReintento() {

        String mensaje = """
                {
                    "usuarioId": 999,
                    "nombre": "Usuario prueba reintento",
                    "email": "reintento@prueba.cl",
                    "autorizado": false,
                    "rol": "FORZAR_REINTENTO",
                    "evento": "FORZAR_REINTENTO"
                }
                """;

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                mensaje
        );
    }
}