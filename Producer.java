import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;
import java.net.URISyntaxException;
import java.util.Set;

public class Producer implements Runnable {

    private String topicName;
    private String name;
    private News news;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private ActiveMQConnection connection;
    private Set<ActiveMQTopic> allTopics;


    public Producer(String name, News news) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = news.getDomain();
        this.news = news;
        connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        allTopics = connection.getDestinationSource().getTopics();
    }

    public void run() {

        try {
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(this.topicName);
            MessageProducer producer = session.createProducer(destination);

            ObjectMessage message = session.createObjectMessage();
            message.setObject(this.news);
            producer.send(message);
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}