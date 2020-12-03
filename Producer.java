import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Producer {
    public static void main(String []args) throws JMSException, URISyntaxException, IOException {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Type your name: ");
        String producerName = keyboard.readLine();
        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        ActiveMQConnection connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        Set<ActiveMQTopic> allTopics = connection.getDestinationSource().getTopics();
        System.out.println("All topics are :");
        for (ActiveMQTopic topic : allTopics){
            System.out.println(topic.getTopicName());
        }
        System.out.println("Select the topic where you want to write OR type -> new topic to create a new topic");
        String topicName = keyboard.readLine();
        if(topicName.equals("new topic")){
            System.out.println("Type the name of topic");
            topicName = keyboard.readLine();
        }
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        System.out.print("Set news domain: ");
        String domain = keyboard.readLine();
        System.out.print("Text: ");
        String text = keyboard.readLine();

        News theNews = new News(domain,producerName,text);
        ObjectMessage message = session.createObjectMessage();
        message.setObject(theNews);
        producer.send(message);
        session.close();
        connection.close();
    }

}