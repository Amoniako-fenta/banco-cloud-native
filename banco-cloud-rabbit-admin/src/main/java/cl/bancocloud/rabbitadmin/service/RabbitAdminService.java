package cl.bancocloud.rabbitadmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    RabbitAdminService.class
            );

    private final AmqpAdmin rabbitAdmin;

    public RabbitAdminService(
            AmqpAdmin rabbitAdmin
    ) {
        this.rabbitAdmin = rabbitAdmin;
    }


    // ==========================
    // QUEUES
    // ==========================

    public void crearQueue(
            String nombre
    ) {

        log.info(
                "Intentando crear cola: {}",
                nombre
        );

        Queue queue =
                QueueBuilder
                        .durable(nombre)
                        .build();

        String resultado =
                rabbitAdmin.declareQueue(queue);

        log.info(
                "Cola creada correctamente: {} - resultado: {}",
                nombre,
                resultado
        );
    }


    public boolean eliminarQueue(
            String nombre
    ) {

        boolean resultado =
                rabbitAdmin.deleteQueue(nombre);

        log.info(
                "Cola {} eliminada: {}",
                nombre,
                resultado
        );

        return resultado;
    }


    // ==========================
    // EXCHANGES
    // ==========================

    public void crearExchange(
            String nombre
    ) {

        log.info(
                "Intentando crear exchange: {}",
                nombre
        );

        DirectExchange exchange =
                new DirectExchange(
                        nombre,
                        true,
                        false
                );

        rabbitAdmin.declareExchange(
                exchange
        );

        log.info(
                "Exchange creado correctamente: {}",
                nombre
        );
    }


    public boolean eliminarExchange(
            String nombre
    ) {

        boolean resultado =
                rabbitAdmin.deleteExchange(
                        nombre
                );

        log.info(
                "Exchange {} eliminado: {}",
                nombre,
                resultado
        );

        return resultado;
    }


    // ==========================
    // BINDINGS
    // ==========================

    public void crearBinding(
            String queueName,
            String exchangeName,
            String routingKey
    ) {

        Queue queue =
                new Queue(
                        queueName,
                        true
                );

        DirectExchange exchange =
                new DirectExchange(
                        exchangeName,
                        true,
                        false
                );

        Binding binding =
                BindingBuilder
                        .bind(queue)
                        .to(exchange)
                        .with(routingKey);

        rabbitAdmin.declareBinding(
                binding
        );

        log.info(
                "Binding creado: queue={}, exchange={}, routingKey={}",
                queueName,
                exchangeName,
                routingKey
        );
    }


    public void eliminarBinding(
            String queueName,
            String exchangeName,
            String routingKey
    ) {

        Queue queue =
                new Queue(
                        queueName,
                        true
                );

        DirectExchange exchange =
                new DirectExchange(
                        exchangeName,
                        true,
                        false
                );

        Binding binding =
                BindingBuilder
                        .bind(queue)
                        .to(exchange)
                        .with(routingKey);

        rabbitAdmin.removeBinding(
                binding
        );

        log.info(
                "Binding eliminado: queue={}, exchange={}, routingKey={}",
                queueName,
                exchangeName,
                routingKey
        );
    }
}
