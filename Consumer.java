package com.company;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQTopic;
import javax.jms.*;
import java.net.URISyntaxException;
import java.util.Set;


public class Consumer implements Runnable {

    private String name;
    private String topicName;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private ActiveMQConnection connection;
    private Set<ActiveMQTopic> allTopics ;
    private Session session;
    private String confirmTopic;


    public Consumer(String name, String topicName, String confirmTopic) throws URISyntaxException, JMSException {
        this.name = name;
        this.topicName = topicName;
        this.confirmTopic=confirmTopic;
        this.connection = ActiveMQConnection.makeConnection(url);
        this.connection.setClientID(this.name);
        connection.start();
        allTopics = connection.getDestinationSource().getTopics();
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
    public String getConfirmTopic(){
        return this.confirmTopic;
    }

    @Override
    public void run() {
        try {
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topicDestination = session.createTopic(this.topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topicDestination,this.name);
            consumer.setMessageListener(new Listener(name));

            Destination destination = session.createTopic(this.getConfirmTopic());
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(this.getConfirmTopic());
            producer.send(message);

            closeConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
