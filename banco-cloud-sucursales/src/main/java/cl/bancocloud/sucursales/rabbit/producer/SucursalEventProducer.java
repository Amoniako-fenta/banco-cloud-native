package cl.bancocloud.sucursales.rabbit.producer;

import cl.bancocloud.sucursales.entity.Sucursal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SucursalEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(SucursalEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.sucursal.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.sucursal.routing-key}")
    private String routingKey;


    public SucursalEventProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void publicarSucursalCreada(
            Sucursal sucursal
    ) {

        /*
         * Creamos el mensaje como texto JSON.
         *
         * Así evitamos depender directamente
         * de ObjectMapper/Jackson.
         */
        String mensaje = String.format(
                """
                {
                    "sucursalId": %d,
                    "nombre": "%s",
                    "comuna": "%s",
                    "region": "%s",
                    "evento": "SUCURSAL_CREADA"
                }
                """,
                sucursal.getId(),
                escapar(sucursal.getNombre()),
                escapar(sucursal.getComuna()),
                escapar(sucursal.getRegion())
        );

        try {

            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    mensaje
            );


            log.info(
                    "Evento SUCURSAL_CREADA enviado a RabbitMQ: {}",
                    mensaje
            );


        } catch (AmqpException e) {

            /*
             * Si RabbitMQ falla,
             * la sucursal ya quedó guardada.
             *
             * Registramos el problema,
             * pero no detenemos toda
             * la operación de negocio.
             */

            log.error(
                    "No fue posible enviar el evento de sucursal a RabbitMQ: {}",
                    e.getMessage()
            );
        }
    }


    /*
     * Evita romper el JSON si un texto
     * contiene comillas.
     */
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