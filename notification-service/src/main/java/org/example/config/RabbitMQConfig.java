package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ORDER_NOTIFICATION = "order.notification.queue";
    public static final String EXCHANGE_ORDERS = "order.exchange";
    public static final String ROUTING_KEY_ORDER_CREATED = "order.created";
    public static final String ROUTING_KEY_ORDER_UPDATED = "order.updated";

    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(QUEUE_ORDER_NOTIFICATION, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(EXCHANGE_ORDERS);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderNotificationQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(orderNotificationQueue)
                .to(orderExchange)
                .with(ROUTING_KEY_ORDER_CREATED);
    }

    @Bean
    public Binding orderUpdatedBinding(Queue orderNotificationQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(orderNotificationQueue)
                .to(orderExchange)
                .with(ROUTING_KEY_ORDER_UPDATED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}