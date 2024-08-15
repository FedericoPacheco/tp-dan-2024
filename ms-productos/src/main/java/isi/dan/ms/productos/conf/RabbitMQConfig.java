package isi.dan.ms.productos.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Video RabbitMQ + test containers: https://www.youtube.com/watch?v=9jZInwFtp44

    public static final String ORDENES_COMPRA_QUEUE = "cola-ordenes-compra";
    public static final String ORDENES_COMPRA_ROUTING_KEY = "orden.compra";
    public static final String ORDENES_PROVISION_QUEUE = "cola-ordenes-provision";
    public static final String ORDENES_PROVISION_ROUTING_KEY = "orden.provision";
    public static final String ORDENES_EXCHANGE = "OrdenesExchange";

    @Bean
    public Queue ordenesCompraQueue() {
        return new Queue(ORDENES_COMPRA_QUEUE, true);
    }

    @Bean
    public Queue ordenesProvisionQueue() {
        return new Queue(ORDENES_PROVISION_QUEUE, true);
    }

    // Agente/broker que se encarga de distribuir los mensajes en las colas
    @Bean
    public TopicExchange ordenesExchange() {
        return new TopicExchange(ORDENES_EXCHANGE);
    }

    // Relacionar colas con exchange
    @Bean
    Binding ordenesCompraQueueBinding(Queue ordenesCompraQueue, TopicExchange ordenesExchange) {
        return BindingBuilder.bind(ordenesCompraQueue).to(ordenesExchange).with(ORDENES_COMPRA_ROUTING_KEY);
    }

    @Bean
    Binding ordenesProvisionQueueBinding(Queue ordenesProvisionQueue, TopicExchange ordenesExchange) {
        return BindingBuilder.bind(ordenesProvisionQueue).to(ordenesExchange).with(ORDENES_PROVISION_ROUTING_KEY);
    }
  
    // Usar jackson para serializar y de-serializar los objetos OrdenCompraDTO y OrdenProvisionDTO
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /* RabbitTemplate: clase que permite enviar (send(), convertAndSend()) 
    y recibir (receive() y convertSendAndReceive()) mensajes asincrona y sincronamente. 
    Se le setea a propósito el conversor a json.
    */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(this.jsonMessageConverter());
        return rabbitTemplate;
    }
}

