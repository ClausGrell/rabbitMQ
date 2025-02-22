package com.example.rabbitmq;

import com.example.rabbitmq.service.MessageListenerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.amqp.core.Message;
import com.rabbitmq.client.Channel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MessageListenerTest {

	String testMessage = "{\n" +
			"  \"event\": \"s3:ObjectCreated:Put\",\n" +
			"  \"bucket\": {\n" +
			"    \"name\": \"example-bucket\"\n" +
			"  },\n" +
			"  \"object\": {\n" +
			"    \"key\": \"path/to/object/file.txt\",\n" +
			"    \"size\": 2048,\n" +
			"    \"etag\": \"d41d8cd98f00b204e9800998ecf8427e\",\n" +
			"    \"content-type\": \"text/plain\"\n" +
			"  },\n" +
			"  \"timestamp\": \"2025-02-22T12:00:00Z\",\n" +
			"  \"user_metadata\": {\n" +
			"    \"author\": \"Jane Doe\",\n" +
			"    \"tags\": [\"document\", \"file\"]\n" +
			"  },\n" +
			"  \"aws_region\": \"us-west-2\",\n" +
			"  \"event_source\": \"aws:s3\",\n" +
			"  \"event_source_arn\": \"arn:aws:s3:::example-bucket\",\n" +
			"  \"request_id\": \"abcd1234efgh5678\",\n" +
			"  \"source_ip_address\": \"192.168.1.100\"\n" +
			"}\n";
	@Autowired
	private MessageListenerService messageListenerService;  // The listener we want to test

	@Mock
	private RabbitTemplate rabbitTemplate; // Mock RabbitTemplate (if needed for some reason)

	@Test
	void testMessageListenerWithRealRabbitMQ() throws Exception {
		// Send a normal message to the queue
		String messageContent = testMessage;
		rabbitTemplate.convertAndSend("testQ", messageContent);

		// Manually create a Message object similar to the one that the listener will receive
		Message testMessage = new Message(messageContent.getBytes());

		// Here we are mocking the Channel that is passed to the listener method
		Channel mockChannel = mock(Channel.class);

		// Simulate the message being processed by the listener
		messageListenerService.receiveMessage(testMessage,mockChannel);

		// Verify that the basicAck method was called on the Channel (message successfully acknowledged)
		verify(mockChannel, times(1)).basicAck(anyLong(), eq(false));
	}}