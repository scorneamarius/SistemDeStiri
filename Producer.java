import javafx.collections.ObservableMap;
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
    private ObservableMap<String, String> observableMap;


    public Producer(String name, News news,  ObservableMap<String, String> observableMap) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = news.getDomain();
        this.news = news;
        news.setAuthor(this.name);
        connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        this.observableMap = observableMap;
        allTopics = connection.getDestinationSource().getTopics();
    }

    public int getNumberOfSubscribers(String topicName){
        int nr=0;
        for (ObservableMap.Entry<String,String> entry : observableMap.entrySet()){
            if(entry.getValue().equals(topicName)){
                nr++;
            }
        }
        return nr;
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