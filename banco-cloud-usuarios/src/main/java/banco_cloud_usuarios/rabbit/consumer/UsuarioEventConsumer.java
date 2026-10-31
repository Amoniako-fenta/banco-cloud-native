package banco_cloud_usuarios.rabbit.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UsuarioEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(UsuarioEventConsumer.class);

    private static final String HEADER_REINTENTOS =
            "x-retry-count";

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.usuario.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.usuario.routing-key}")
    private String routingKey;

    @Value("${bancocloud.rabbit.usuario.max-retries:3}")
    private int maxReintentos;

    public UsuarioEventConsumer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(
            queues = "${bancocloud.rabbit.usuario.queue}"
    )
    public void recibirEvento(
            String mensaje,
            Message message,
            Channel channel
    ) throws IOException {

        long deliveryTag =
                message
                        .getMessageProperties()
                        .getDeliveryTag();

        try {

            log.info(
                    "Evento de usuario recibido desde RabbitMQ: {}",
                    mensaje
            );

            procesar(mensaje);

            channel.basicAck(
                    deliveryTag,
                    false
            );

            log.info(
                    "Mensaje de usuario procesado correctamente. ACK enviado."
            );

        } catch (MensajeInvalidoException e) {

            log.error(
                    "Error no recuperable en mensaje de usuario: {}",
                    e.getMessage()
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );

            log.warn(
                    "NACK enviado. Mensaje de usuario enviado a la DLQ."
            );

        } catch (Exception e) {

            manejarErrorRecuperable(
                    message,
                    channel,
                    deliveryTag,
                    e
            );
        }
    }

    private void procesar(
            String mensaje
    ) {

        if (
                mensaje == null ||
                mensaje.isBlank()
        ) {
            throw new MensajeInvalidoException(
                    "El mensaje está vacío"
            );
        }

        if (mensaje.contains("FORZAR_ERROR")) {

            throw new MensajeInvalidoException(
                    "Error intencional para prueba de DLQ"
            );
        }

        if (mensaje.contains("FORZAR_REINTENTO")) {

            throw new ErrorTemporalException(
                    "Error temporal intencional para prueba de reintentos"
            );
        }

        if (!mensaje.contains("\"usuarioId\"")) {

            throw new MensajeInvalidoException(
                    "El mensaje no contiene usuarioId"
            );
        }

        if (!mensaje.contains("\"evento\"")) {

            throw new MensajeInvalidoException(
                    "El mensaje no contiene tipo de evento"
            );
        }

        log.info(
                "Contenido del evento de usuario validado correctamente."
        );
    }

    private void manejarErrorRecuperable(
            Message message,
            Channel channel,
            long deliveryTag,
            Exception causa
    ) throws IOException {

        int intentos =
                obtenerReintentos(message);

        if (intentos >= maxReintentos) {

            log.error(
                    "Se agotaron los {} reintentos de usuario. Error: {}",
                    maxReintentos,
                    causa.getMessage()
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );

            log.warn(
                    "NACK enviado. Mensaje enviado a la DLQ después de agotar reintentos."
            );

            return;
        }

        try {

            Message reintento =
                    MessageBuilder
                            .fromMessage(message)
                            .setHeader(
                                    HEADER_REINTENTOS,
                                    intentos + 1
                            )
                            .build();

            rabbitTemplate.send(
                    exchange,
                    routingKey,
                    reintento
            );

            channel.basicAck(
                    deliveryTag,
                    false
            );

            log.warn(
                    "Error recuperable de usuario: {}. Reintento {}/{} enviado.",
                    causa.getMessage(),
                    intentos + 1,
                    maxReintentos
            );

        } catch (AmqpException ex) {

            log.error(
                    "No fue posible reenviar el mensaje para reintento: {}",
                    ex.getMessage()
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );

            log.warn(
                    "NACK enviado. Mensaje derivado a DLQ."
            );
        }
    }

    private int obtenerReintentos(
            Message message
    ) {

        Object valor =
                message
                        .getMessageProperties()
                        .getHeader(
                                HEADER_REINTENTOS
                        );

        return valor instanceof Number numero
                ? numero.intValue()
                : 0;
    }

    static class MensajeInvalidoException
            extends RuntimeException {

        MensajeInvalidoException(
                String mensaje
        ) {
            super(mensaje);
        }
    }

    static class ErrorTemporalException
            extends RuntimeException {

        ErrorTemporalException(
                String mensaje
        ) {
            super(mensaje);
        }
    }
}