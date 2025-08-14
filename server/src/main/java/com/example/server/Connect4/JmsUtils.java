package com.example.server.connect4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * JMS queue configuration for the Connect 4 game.
 * Defines beans for the different difficulty-level queues.
 */
@Configuration
public class JmsUtils {

    @Bean
    public Queue easyQueue() {
        return new ActiveMQQueue("connect4easy.queue");
    }
    
    @Bean
    public Queue mediumQueue() {
        return new ActiveMQQueue("connect4medium.queue");
    }

    @Bean
    public Queue hardQueue() {
        return new ActiveMQQueue("connect4hard.queue");
    } 
}
