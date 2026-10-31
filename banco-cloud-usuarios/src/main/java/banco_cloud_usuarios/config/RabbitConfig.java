package banco_cloud_usuarios.config;

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

    @Value("${bancocloud.rabbit.usuario.exchange}")
    private String usuarioExchange;

    @Value("${bancocloud.rabbit.usuario.queue}")
    private String usuarioQueue;

    @Value("${bancocloud.rabbit.usuario.routing-key}")
    private String usuarioRoutingKey;

    @Value("${bancocloud.rabbit.usuario.dlx}")
    private String usuarioDlx;

    @Value("${bancocloud.rabbit.usuario.dlq}")
    private String usuarioDlq;

    @Value("${bancocloud.rabbit.usuario.dlq-routing-key}")
    private String usuarioDlqRoutingKey;


    @Bean
    public DirectExchange usuarioExchange() {
        return new DirectExchange(
                usuarioExchange,
                true,
                false
        );
    }


    @Bean
    public DirectExchange usuarioDeadLetterExchange() {
        return new DirectExchange(
                usuarioDlx,
                true,
                false
        );
    }


    @Bean
    public Queue usuarioQueue() {
        return QueueBuilder
                .durable(usuarioQueue)
                .deadLetterExchange(usuarioDlx)
                .deadLetterRoutingKey(usuarioDlqRoutingKey)
                .build();
    }


    @Bean
    public Queue usuarioDeadLetterQueue() {
        return QueueBuilder
                .durable(usuarioDlq)
                .build();
    }


    @Bean
    public Binding usuarioBinding() {
        return BindingBuilder
                .bind(usuarioQueue())
                .to(usuarioExchange())
                .with(usuarioRoutingKey);
    }


    @Bean
    public Binding usuarioDeadLetterBinding() {
        return BindingBuilder
                .bind(usuarioDeadLetterQueue())
                .to(usuarioDeadLetterExchange())
                .with(usuarioDlqRoutingKey);
    }
}