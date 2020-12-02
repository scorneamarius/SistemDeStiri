import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.http.WebSocket;
import java.util.Set;

public class Consumer {
    public static void main(String []args) throws JMSException, URISyntaxException, IOException {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        ActiveMQConnection connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        Set<ActiveMQTopic> allTopics = connection.getDestinationSource().getTopics();
        System.out.println("All topics are :");
        for (ActiveMQTopic topic : allTopics){
            System.out.println(topic.getTopicName());
        }
        System.out.println("Type topic which you want to read");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String topicName = keyboard.readLine();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new Listener());
//       connection.close();

    }
}