package isi.dan.ms_productos.conf;

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

    public static final String ORDENES_COMPRA_QUEUE = "cola-ordenes-compra";
    public static final String ORDENES_COMPRA_ROUTING_KEY = "orden.compra";
    public static final String ORDENES_COMPRA_EXCHANGE = "OrdenesCompraExchange";

    //Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    // Cola de ordenes de compra
    @Bean
    public Queue ordenesCompraQueue() {
        return new Queue(ORDENES_COMPRA_QUEUE, true);
    }

    // Agente/broker que se encarga de distribuir los mensajes en las colas
    @Bean
    public TopicExchange ordenesCompraExchange() {
        return new TopicExchange(ORDENES_COMPRA_EXCHANGE);
    }

    // Relacionar cola con exchange
    @Bean
    Binding ordenesCompraQueueBinding(Queue ordenesCompraQueue, TopicExchange ordenesCompraExchange) {
        return BindingBuilder.bind(ordenesCompraQueue).to(ordenesCompraExchange).with(ORDENES_COMPRA_ROUTING_KEY);
    }

    // Usar jackson para serializar y de-serializar los objetos OrdenCompraDTO
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

    /* Referencias:
     * https://stackoverflow.com/questions/75362841/spring-boot-rabbit-mq-could-not-autowire-connectionfactory
     * https://www.springcloud.io/post/2022-03/messaging-using-rabbitmq-in-spring-boot-application/#gsc.tab=0
     * Edit: no es necesaria, la genera automáticamente spring
     * /
    /* 
    @Bean
    public ConnectionFactory connectionFactory(
        @Value("${spring.rabbitmq.host}") String host,
        @Value("${spring.rabbitmq.port}") String port,
        @Value("${spring.rabbitmq.username}") String username,
        @Value("${spring.rabbitmq.password}") String password
    ) 
    {
        log.info("CONNECTION FACTORY: host: " + host + "; port: " + port + "; username: " + username + "; password: " + password);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(Integer.valueOf(port));
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        return connectionFactory;
    }
    */
}

