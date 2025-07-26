package com.example.server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;


@Configuration
public class JmsUtils {
   @Bean
    public Queue gameQueue() {
        return new ActiveMQQueue("connect4easy.queue");
    }  
}
