package cl.bancocloud.sucursales.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${bancocloud.rabbit.sucursal.exchange}")
    private String sucursalExchange;

    @Value("${bancocloud.rabbit.sucursal.queue}")
    private String sucursalQueue;

    @Value("${bancocloud.rabbit.sucursal.routing-key}")
    private String sucursalRoutingKey;

    @Value("${bancocloud.rabbit.sucursal.dlx}")
    private String sucursalDlx;

    @Value("${bancocloud.rabbit.sucursal.dlq}")
    private String sucursalDlq;

    @Value("${bancocloud.rabbit.sucursal.dlq-routing-key}")
    private String sucursalDlqRoutingKey;


    // ============================
    // EXCHANGE PRINCIPAL
    // ============================

    @Bean
    public DirectExchange sucursalExchange() {

        return new DirectExchange(
                sucursalExchange,
                true,
                false
        );
    }


    // ============================
    // DEAD LETTER EXCHANGE
    // ============================

    @Bean
    public DirectExchange sucursalDeadLetterExchange() {

        return new DirectExchange(
                sucursalDlx,
                true,
                false
        );
    }


    // ============================
    // COLA PRINCIPAL
    // ============================

    @Bean
    public Queue sucursalQueue() {

        return QueueBuilder
                .durable(sucursalQueue)
                .deadLetterExchange(sucursalDlx)
                .deadLetterRoutingKey(sucursalDlqRoutingKey)
                .build();
    }


    // ============================
    // DEAD LETTER QUEUE
    // ============================

    @Bean
    public Queue sucursalDeadLetterQueue() {

        return QueueBuilder
                .durable(sucursalDlq)
                .build();
    }


    // ============================
    // BINDING PRINCIPAL
    // ============================

    @Bean
    public Binding sucursalBinding() {

        return BindingBuilder
                .bind(sucursalQueue())
                .to(sucursalExchange())
                .with(sucursalRoutingKey);
    }


    // ============================
    // BINDING DLQ
    // ============================

    @Bean
    public Binding sucursalDeadLetterBinding() {

        return BindingBuilder
                .bind(sucursalDeadLetterQueue())
                .to(sucursalDeadLetterExchange())
                .with(sucursalDlqRoutingKey);
    }
}