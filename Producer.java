import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Set;

public class Producer {
    public static void main(String []args) throws JMSException, URISyntaxException, IOException {
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        ActiveMQConnection connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        Set<ActiveMQTopic> allTopics = connection.getDestinationSource().getTopics();
        System.out.println("All topics are :");
        for (ActiveMQTopic topic : allTopics){
            System.out.println(topic.getTopicName());
        }
        System.out.println("Select the topic where you want to write OR type -> new topic to create a new topic");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String topicName = keyboard.readLine();
        if(topicName.equals("new topic")){
            System.out.println("Type the name of topic");
            topicName = keyboard.readLine();
        }
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        System.out.println("Type the message");
        String textMessage = keyboard.readLine();
        TextMessage message = session.createTextMessage(textMessage);
        producer.send(message, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
        session.close();
        connection.close();
    }

}