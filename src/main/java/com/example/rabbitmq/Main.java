package com.example.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

/**
 * Simple messaging example with RabbitMQ SSL.
 * Guide from https://www.rabbitmq.com/ssl.html
 * @author Andreas Sekulski
 */
public class Main {

    /**
     * Client certificate as pkcs #12 file format.
     */
    private static final String CLIENT_CERTIFICATE = "rabbitmq.key";

    /**
     * Client password from certificate. This INFORMATION should be stored safely!!!!
     */
    private static final String CLIENT_PASSWORD_CERTIFICATE = "rabbitstore";

    /**
     * Given file format from client certificates.
     */
    private static final String KEYSTORE_CLIENT = "PKCS12";

    /**
     * Server certificate as java keystore.
     */
    private static final String SERVER_CERTIFICATE = "ca.key";

    /**
     * Password from java keystore. This INFORMATION should be stored safely!!!!
     */
    private static final String SERVER_CERTIFICATE_PASSWORD = "rabbitstore";

    /**
     * Java keystore type.
     */
    private static final String SERVER_CERTIFICATE_TYPE = "JKS";
    private static final String TLS_TYPE = "TLSv1.2";
    private static final String RABBIT_MQ_HOST = "192.168.0.184";
    private static final int RABBIT_MQ_PORT = 5672;
    private static final String RABBIT_MQ_USER = "guest";
    private static final String RABBIT_MQ_PASSWORD = "guest";

    /**
     * Rabbitmq example channel to send and receive a message.
     */
    private static final String RABBIT_MQ_CHANNEL = "rabbitmq-java-ssl-test";

    public static void main(String[] args)
    {
        try
        {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RABBIT_MQ_HOST);
            factory.setPort(RABBIT_MQ_PORT);
            factory.setUsername(RABBIT_MQ_USER);
            factory.setPassword(RABBIT_MQ_PASSWORD);

            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();

            //non-durable, exclusive, auto-delete queue
            channel.queueDeclare(RABBIT_MQ_CHANNEL, false, true, true, null);
            channel.basicPublish("", RABBIT_MQ_CHANNEL, null, "Hello SSL World :-)".getBytes());

            GetResponse chResponse = channel.basicGet(RABBIT_MQ_CHANNEL, false);
            if(chResponse == null) {
                System.out.println("No message retrieved");
            } else {
                byte[] body = chResponse.getBody();
                System.out.println("Received: " + new String(body));
            }

            channel.close();
            conn.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
}