package banco_cloud_cajeros.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${bancocloud.rabbit.cajero.exchange}")
    private String cajeroExchange;

    @Value("${bancocloud.rabbit.cajero.queue}")
    private String cajeroQueue;

    @Value("${bancocloud.rabbit.cajero.routing-key}")
    private String cajeroRoutingKey;

    @Value("${bancocloud.rabbit.cajero.dlx}")
    private String cajeroDlx;

    @Value("${bancocloud.rabbit.cajero.dlq}")
    private String cajeroDlq;

    @Value("${bancocloud.rabbit.cajero.dlq-routing-key}")
    private String cajeroDlqRoutingKey;


    @Bean
    public DirectExchange cajeroExchange() {

        return new DirectExchange(
                cajeroExchange,
                true,
                false
        );
    }


    @Bean
    public DirectExchange cajeroDeadLetterExchange() {

        return new DirectExchange(
                cajeroDlx,
                true,
                false
        );
    }


    @Bean
    public Queue cajeroQueue() {

        return QueueBuilder
                .durable(cajeroQueue)
                .deadLetterExchange(cajeroDlx)
                .deadLetterRoutingKey(cajeroDlqRoutingKey)
                .build();
    }


    @Bean
    public Queue cajeroDeadLetterQueue() {

        return QueueBuilder
                .durable(cajeroDlq)
                .build();
    }


    @Bean
    public Binding cajeroBinding() {

        return BindingBuilder
                .bind(cajeroQueue())
                .to(cajeroExchange())
                .with(cajeroRoutingKey);
    }


    @Bean
    public Binding cajeroDeadLetterBinding() {

        return BindingBuilder
                .bind(cajeroDeadLetterQueue())
                .to(cajeroDeadLetterExchange())
                .with(cajeroDlqRoutingKey);
    }
}