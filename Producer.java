import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {
    public static void main(String []args) throws JMSException
    {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        String queueName = "MESSAGE_QUEUE";
        System.out.println(url);
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage("It works");
        for(int i=0;i<5;i++)
        {
            TextMessage messages = session.createTextMessage(message.getText()+ i);
            producer.send(messages);
        }
        connection.close();
    }

}