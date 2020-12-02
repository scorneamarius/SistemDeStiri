import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.http.WebSocket;

public class Consumer {
    public static void main(String []args)throws JMSException {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        String queueName = "MESSAGE_QUEUE";
        System.out.println(url);

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);
        MessageConsumer consumer = session.createConsumer(destination);
        TextMessage message;

        Listener myListener = new Listener();
        consumer.setMessageListener(myListener);
        connection.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // connection.close();
    }
}