import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Producer implements Runnable {

    private String topicName;
    private String topicAlert;
    private String name;
    private News news;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private ActiveMQConnection connection;
    private Set<ActiveMQTopic> allTopics;
    private Session session;
    private Map<News,Integer> map;
    private ObservableMap<News,Integer> observableMap;

    public Producer(News news, String topicName, String topicAlert) throws URISyntaxException, JMSException {
        this.news = news;
        this.topicName = topicName;
        this.topicAlert = topicAlert;
        map = new HashMap<News, Integer>();
        observableMap = FXCollections.observableMap(map);
        observableMap.addListener(new myMapChangeListener());
        observableMap.put(news, 0);
        connection = ActiveMQConnection.makeConnection(url);
        connection.start();
    }

    public void run() {

        try {
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(this.topicName);
            MessageProducer producer = session.createProducer(destination);

            Topic destination1 = session.createTopic(this.topicAlert);
            MessageConsumer listenerTopicAlert = session.createConsumer(destination1);

            ObjectMessage message = session.createObjectMessage();
            message.setObject(this.news);
            producer.send(message);

            listenerTopicAlert.setMessageListener(new Listener(observableMap));


        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}