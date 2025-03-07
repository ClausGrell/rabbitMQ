package com.example.rabbitmq;

import com.rabbitmq.client.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeoutException;

public class RabbitMQSSLTest {

    private static final String QUEUE_NAME = "test_queue";
    private static final String RABBITMQ_HOST = "192.168.0.184";
    private static final int SSL_PORT = 5672; // SSL port

    public static void main(String[] args) throws Exception {
        // Set up SSL context
        SSLContext sslContext = setupSSLContext();

        // Create connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(SSL_PORT);
        factory.useSslProtocol(sslContext);

        // Optional: set up credentials
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the queue (it must be the same name as used in the producer/consumer)
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Connected to RabbitMQ over SSL");

            // Send a message to the queue
            String message = "Hello, RabbitMQ over SSL!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent: " + message);

            // Consume the message
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            if (response != null) {
                String receivedMessage = new String(response.getBody());
                System.out.println("Received: " + receivedMessage);
            } else {
                System.out.println("No message received.");
            }
        }
    }

    /**
     * Set up the SSL context using the certificates
     */
    private static SSLContext setupSSLContext() throws Exception {
        // Path to your certificate files
        String caCertPath = "ssl/cacerts"; // CA certificate
        String clientKeyStorePath = "ssl/rabbitmq.jks"; // PKCS12 client certificate and key file

        // Load the PKCS12 keystore (which contains the client certificate and private key)
        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        FileInputStream clientCertFile = new FileInputStream(clientKeyStorePath);
        clientKeyStore.load(clientCertFile, "S0r0ver!".toCharArray()); // Use the password for the PKCS12 file

        // Create a TrustStore to trust the CA certificate
        KeyStore trustKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustKeyStore.load(null, null); // Initialize an empty TrustStore

        // Load CA certificate into the TrustStore
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        FileInputStream caCertFile = new FileInputStream(caCertPath);

        FileInputStream caCertFile2 = new FileInputStream("ssl/fullchain.pem");
        CertificateFactory certFactory2 = CertificateFactory.getInstance("X.509");
        X509Certificate caCert2 = (X509Certificate) certFactory2.generateCertificate(caCertFile2);

        String alias = caCert2.getSubjectDN().getName();

      //  X509Certificate caCert = (X509Certificate) certFactory.generateCertificate(caCertFile);
        trustKeyStore.setCertificateEntry(alias, caCert2);

        // Initialize a TrustManagerFactory with the TrustStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustKeyStore);

        // Initialize a KeyManagerFactory with the client certificate (from the PKCS12 keystore)
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, "S0r0ver!".toCharArray()); // Use the password for the keystore

        // Create and initialize the SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;    }
}

