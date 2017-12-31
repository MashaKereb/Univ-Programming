package mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class MQClient {
    private Connection connection;
    private String requestQueueName = "rpc_queue";
    private ConnectionFactory factory;
    private Channel channel;
    private String replyQueueName;
    private String corrId;
    private final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

    public MQClient() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
    }

    public byte[] call(byte[] data) throws IOException, InterruptedException, TimeoutException {

        corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, data);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               if (properties.getCorrelationId().equals(corrId)) {

                    response.offer(new String(body, "ISO-8859-1"));

                }
            }
        });
        return response.take().getBytes("ISO-8859-1");
    }

    public void close() throws IOException {
        connection.close();
    }
}
