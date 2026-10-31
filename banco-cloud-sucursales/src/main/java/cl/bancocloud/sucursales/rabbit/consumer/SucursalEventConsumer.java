package cl.bancocloud.sucursales.rabbit.consumer;

import cl.bancocloud.sucursales.rabbit.event.SucursalEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Consumidor de eventos del dominio "sucursales".
 *
 * Manejo de errores (ACK manual):
 *  - OK                      -> ACK.
 *  - Error NO recuperable    -> NACK sin requeue -> la cola lo envia a la DLQ (DLX configurado en RabbitConfig).
 *                              (JSON invalido, campos obligatorios faltantes, evento desconocido)
 *  - Error recuperable       -> se reenvia a la cola principal con el header x-retry-count + 1 y se hace ACK
 *                              del original. Si supera max-retries -> NACK sin requeue -> DLQ.
 *
 * Todo queda registrado en logs.
 */
@Component
public class SucursalEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(SucursalEventConsumer.class);

    private static final String HEADER_REINTENTOS = "x-retry-count";

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Value("${bancocloud.rabbit.sucursal.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.sucursal.routing-key}")
    private String routingKey;

    @Value("${bancocloud.rabbit.sucursal.max-retries:3}")
    private int maxReintentos;


    public SucursalEventConsumer(
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }


    @RabbitListener(queues = "${bancocloud.rabbit.sucursal.queue}")
    public void recibirEvento(
            String mensaje,
            Message message,
            Channel channel
    ) throws IOException {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {

            log.info("Evento de sucursal recibido: {}", mensaje);

            procesar(mensaje);

            channel.basicAck(deliveryTag, false);

            log.info("Evento de sucursal procesado correctamente. ACK enviado.");

        } catch (MensajeInvalidoException e) {

            // Error NO recuperable: reintentar no sirve. Directo a la DLQ.
            log.error("Mensaje invalido, se envia a la DLQ: {}", e.getMessage());

            channel.basicNack(deliveryTag, false, false);

        } catch (Exception e) {

            // Error recuperable: se reintenta hasta maxReintentos.
            manejarErrorRecuperable(message, channel, deliveryTag, e);
        }
    }


    private void procesar(String mensaje) {

        SucursalEvent evento;

        try {
            evento = objectMapper.readValue(mensaje, SucursalEvent.class);
        } catch (Exception e) {
            throw new MensajeInvalidoException("JSON invalido: " + e.getMessage());
        }

        /*
         * Pruebas de DLQ / reintentos (se pueden disparar desde los
         * endpoints de prueba enviando estos textos en el nombre).
         */
        if (mensaje.contains("FORZAR_ERROR")) {
            throw new MensajeInvalidoException("Error intencional para prueba de DLQ");
        }

        if (mensaje.contains("FORZAR_REINTENTO")) {
            throw new ErrorTemporalException("Error temporal intencional para prueba de reintentos");
        }

        if (evento.sucursalId() == null) {
            throw new MensajeInvalidoException("El evento no trae sucursalId");
        }

        if (evento.evento() == null || evento.evento().isBlank()) {
            throw new MensajeInvalidoException("El evento no trae el tipo de evento");
        }

        // Aqui va la reaccion de negocio (ej. notificar, generar ticket). Por ahora se registra.
        log.info(
                "Sucursal {} ({}) - evento {} - comuna {} - region {}",
                evento.sucursalId(),
                evento.nombre(),
                evento.evento(),
                evento.comuna(),
                evento.region()
        );
    }


    private void manejarErrorRecuperable(
            Message message,
            Channel channel,
            long deliveryTag,
            Exception causa
    ) throws IOException {

        int intentos = obtenerReintentos(message);

        if (intentos >= maxReintentos) {

            log.error(
                    "Se agotaron los {} reintentos ({}). Se envia a la DLQ.",
                    maxReintentos,
                    causa.getMessage()
            );

            channel.basicNack(deliveryTag, false, false);
            return;
        }

        try {

            Message reintento = MessageBuilder
                    .fromMessage(message)
                    .setHeader(HEADER_REINTENTOS, intentos + 1)
                    .build();

            rabbitTemplate.send(exchange, routingKey, reintento);

            channel.basicAck(deliveryTag, false);

            log.warn(
                    "Error recuperable ({}). Reintento {}/{} enviado a la cola.",
                    causa.getMessage(),
                    intentos + 1,
                    maxReintentos
            );

        } catch (AmqpException ex) {

            // No se pudo reencolar: se manda a la DLQ para no perder el mensaje.
            log.error("No se pudo reencolar el mensaje, se envia a la DLQ: {}", ex.getMessage());

            channel.basicNack(deliveryTag, false, false);
        }
    }


    private int obtenerReintentos(Message message) {

        Object valor = message
                .getMessageProperties()
                .getHeader(HEADER_REINTENTOS);

        return valor instanceof Number numero ? numero.intValue() : 0;
    }


    /** Error que no se arregla reintentando (mensaje mal formado). */
    static class MensajeInvalidoException extends RuntimeException {
        MensajeInvalidoException(String mensaje) {
            super(mensaje);
        }
    }

    /** Error transitorio (ej. dependencia caida): vale la pena reintentar. */
    static class ErrorTemporalException extends RuntimeException {
        ErrorTemporalException(String mensaje) {
            super(mensaje);
        }
    }
}
