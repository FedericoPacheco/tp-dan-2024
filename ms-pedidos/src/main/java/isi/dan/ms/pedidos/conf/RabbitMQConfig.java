package isi.dan.ms.pedidos.conf;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COLA_ACTUALIZACION_STOCK = "cola-actualizacion-stock";

    @Bean
    public Queue colaActualizacionStock() {
        return new Queue(COLA_ACTUALIZACION_STOCK, true);
    }

    // Usar jackson para serializar y de-serializar los objetos ActualizacionStockDTO
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
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}

