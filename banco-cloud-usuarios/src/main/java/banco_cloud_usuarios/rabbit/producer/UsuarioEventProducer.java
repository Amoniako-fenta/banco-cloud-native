package banco_cloud_usuarios.rabbit.producer;

import banco_cloud_usuarios.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UsuarioEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(UsuarioEventProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${bancocloud.rabbit.usuario.exchange}")
    private String exchange;

    @Value("${bancocloud.rabbit.usuario.routing-key}")
    private String routingKey;


    public UsuarioEventProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void publicarUsuarioCreado(
            Usuario usuario
    ) {

String mensaje = String.format(
        """
        {
            "usuarioId": %d,
            "nombre": "%s",
            "email": "%s",
            "autorizado": %s,
            "rol": "%s",
            "evento": "USUARIO_CREADO"
        }
        """,
        usuario.getId(),
        escapar(usuario.getNombre()),
        escapar(usuario.getEmail()),
        usuario.isAutorizado(),
        escapar(usuario.getRol())
);

        try {

            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    mensaje
            );

            log.info(
                    "Evento USUARIO_CREADO enviado a RabbitMQ: {}",
                    mensaje
            );

        } catch (AmqpException e) {

            /*
             * El usuario ya puede quedar guardado aunque
             * RabbitMQ esté temporalmente caído.
             */
            log.error(
                    "No fue posible enviar el evento de usuario a RabbitMQ: {}",
                    e.getMessage()
            );
        }
    }


    private String escapar(String valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}