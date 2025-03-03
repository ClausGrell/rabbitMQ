package com.example.rabbitmq.config;

import com.example.rabbitmq.service.RabbitListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;


@Configuration
public class RabbitConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @Autowired
    AppConfig appConfig;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  // RabbitMQ hostname
        connectionFactory.setHost("liam.dk");
        connectionFactory.setPort(5772);
        connectionFactory.setUsername("guest"); // RabbitMQ username
        connectionFactory.setPassword("guest"); // RabbitMQ password
        connectionFactory.setVirtualHost("/");

        /*
        try {
            connectionFactory.getRabbitConnectionFactory().useSslProtocol("TLSv1.2");  // Enable TLS v1.2
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
*/
        System.out.println("***************************************************************************");
        return connectionFactory;
    }


    private SSLContext createSslContext() throws Exception {
        // Load the KeyStore (client certificate and private key)
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream keyStoreInputStream = new FileInputStream("path/to/your/rabbitmq.p12")) {
            keyStore.load(keyStoreInputStream, "your-keystore-password".toCharArray());
        }

        // Load the TrustStore (CA certificate)
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream trustStoreInputStream = new FileInputStream("path/to/your/ca.p12")) {
            trustStore.load(trustStoreInputStream, "your-truststore-password".toCharArray());
        }

        // Create and initialize the SSLContext with the KeyStore and TrustStore
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);  // In case mutual authentication is needed, KeyManagers and TrustManagers can be set here

        return sslContext;
    }


    @Bean
    public Queue queue() {
        logger.info("appConfig.getQueueName()=" + appConfig.getQueueName());
        return new Queue(appConfig.getQueueName(), true); // durable queue
    }

    @Bean
    public TopicExchange exchange() {
        logger.info("appConfig.getExchangeName()=" + appConfig.getExchangeName());
        return new TopicExchange(appConfig.getExchangeName());
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        logger.info("Introduce local variable=" + appConfig.getRoutingKey());
        return BindingBuilder.bind(queue).to(exchange).with(appConfig.getRoutingKey());
    }
}
