import javafx.collections.ObservableMap;

import javax.jms.*;

public class Listener implements MessageListener {

    private ObservableMap<News,Integer> observableMap;

    public Listener(ObservableMap observableMap) {
        this.observableMap = observableMap;
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage =(ObjectMessage)message;
        try {
            News news = (News)objectMessage.getObject();
            for (ObservableMap.Entry<News,Integer> entry : observableMap.entrySet()){
            if(entry.getKey().equals(news)){
                entry.setValue(entry.getValue() + 1);
             }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}