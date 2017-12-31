package mq;

import com.rabbitmq.client.*;

import java.io.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by Masha Kereb on 28-May-17.
 */
public class MQServerRunner {
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private static MQServer server = new MQServer();

    public static void main(String[] argv) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    try {
                        int n = ois.readInt();

                        System.out.println(" command (" + n + ")");
                        server.pocessCommand(n, ois, oos);
                        oos.close();
                    } catch (Exception e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        channel.basicPublish("", properties.getReplyTo(), replyProps, baos.toByteArray());
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

            //loop to prevent reaching finally block
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException _ignore) {
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
        }
    }

}
