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

public class Producer implements Runnable {

    private String topicName;
    private String name;
    private News news;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private ActiveMQConnection connection;
    private Set<ActiveMQTopic> allTopics;
    private ObservableMap<String, String> observableMap;
    private Queue<News> newsList = new ConcurrentLinkedQueue<News>();
    private News newsModify=null;



    public Producer(String name, News news,  ObservableMap<String, String> observableMap,Queue<News> newsList) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = news.getDomain();
        this.news = news;
        news.setAuthor(this.name);
        connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        this.observableMap = observableMap;
        allTopics = connection.getDestinationSource().getTopics();
        this.newsList=newsList;
    }

    public Producer(String name, News news,  ObservableMap<String, String> observableMap,Queue<News> newsList,News newsModify) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = news.getDomain();
        this.news = news;
        news.setAuthor(this.name);
        connection = ActiveMQConnection.makeConnection(url);
        connection.start();
        this.observableMap = observableMap;
        allTopics = connection.getDestinationSource().getTopics();
        this.newsList=newsList;
        this.newsModify=newsModify;
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

            if(this.news.type.equals("ADD")) {

                ObjectMessage message = session.createObjectMessage();
                message.setObject(this.news);
                producer.send(message);
                this.newsList.add(this.news);
                session.close();
                connection.close();
            }
            if(this.news.type.equals("MODIFY")&&newsModify!=null){

                this.newsList.remove(news);
                this.newsList.add(newsModify);
            }
            if(this.news.type.equals("DELETE")){

                this.newsList.remove(news);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}