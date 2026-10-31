package banco_cloud_cajeros.rabbit.producer;

import banco_cloud_cajeros.entity.Cajero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CajeroEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(CajeroEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.cajero.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.cajero.routing-key}")
    private String routingKey;

    public CajeroEventProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarCajeroCreado(
            Cajero cajero
    ) {

        String mensaje = String.format(
                """
                {
                    "cajeroId": %d,
                    "nombre": "%s",
                    "comuna": "%s",
                    "region": "%s",
                    "sucursalId": %d,
                    "evento": "CAJERO_CREADO"
                }
                """,
                cajero.getId(),
                escapar(cajero.getNombre()),
                escapar(cajero.getComuna()),
                escapar(cajero.getRegion()),
                cajero.getSucursalId()
        );

        try {

            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    mensaje
            );

            log.info(
                    "Evento CAJERO_CREADO enviado a RabbitMQ: {}",
                    mensaje
            );

        } catch (AmqpException e) {

            log.error(
                    "No fue posible enviar el evento del cajero a RabbitMQ: {}",
                    e.getMessage()
            );
        }
    }

    private String escapar(
            String valor
    ) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}