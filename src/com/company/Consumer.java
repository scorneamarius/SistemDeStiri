package com.company;

import javafx.collections.ObservableMap;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Consumer implements Runnable {

    private String name;
    private String topicName;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private ActiveMQConnection connection;
    private Set<ActiveMQTopic> allTopics ;
    private ObservableMap<String, String> observableMap;
    private Session session;
    private Queue<News> newsList = new ConcurrentLinkedQueue<News>();


    public Consumer(String name, String topicName, ObservableMap<String, String> observableMap,Queue<News> newsList) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = topicName;
        this.connection = ActiveMQConnection.makeConnection(url);
        this.connection.setClientID(this.name);
        connection.start();
        allTopics = connection.getDestinationSource().getTopics();
        this.observableMap = observableMap;
        observableMap.put(this.name, this.topicName);
        this.newsList=newsList;
    }


    private void closeConnection(){
        try {
            Thread.currentThread().sleep(30);
            this.session.close();
            this.connection.close();
        } catch (InterruptedException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void getTopics() throws JMSException {
        System.out.println("All topics are :");
        for (ActiveMQTopic topic : allTopics){
            System.out.println(topic.getTopicName());
        }
    }

    @Override
    public void run() {
        try {
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topicDestination = session.createTopic(this.topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topicDestination,this.name);
            consumer.setMessageListener(new Listener(name));
            System.out.println("consumer");
            closeConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
