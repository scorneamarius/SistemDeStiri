package com.company;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Set;

public class Consumer {

    private String name;
    private TopicConnection connection;
    private TopicSession session;


    public Consumer(String name) {
        this.name = name;
        connect();
    }

    public void connect() {
        try {
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createTopicConnection();

            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close() {

        try {
            if (connection != null) {
                connection.stop();
                session.close();
                connection.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void subscribe() throws IOException,JMSException{

        System.out.println("Type topic which you want to read");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String topicName = keyboard.readLine();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topicDestination = session.createTopic(topicName);


        MessageConsumer consumer = session.createDurableSubscriber(topicDestination,this.getName());
        consumer.setMessageListener(new Listener());


    }
    public String getName(){
        return name;
    }

    public void getTopics()throws URISyntaxException,JMSException {

        String url = ActiveMQConnection.DEFAULT_BROKER_URL;
        ActiveMQConnection connection = ActiveMQConnection.makeConnection(url);
        connection.setClientID(this.getName());
        connection.start();
        Set<ActiveMQTopic> allTopics = connection.getDestinationSource().getTopics();
        System.out.println("All topics are :");
        for (ActiveMQTopic topic : allTopics){
            System.out.println(topic.getTopicName());
        }
    }

    public static void main(String[] args) throws JMSException,URISyntaxException {

        System.out.print("Type your name: ");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String name = null;
        try {
            name = keyboard.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Consumer consumer = new Consumer(name);
        boolean ok = true;

        System.out.print("Type menu");

        while (ok) {

            try {
                BufferedReader keyboard2 = new BufferedReader(new InputStreamReader(System.in));
                String command = null;
                command = keyboard2.readLine();

                if (command.equals("menu")) {
                    System.out.print("-Get all topics\n");
                    System.out.print("-Subscribe\n");
                    System.out.print("-Quit\n");
                }
                if (command.equals("get all topics")) {
                    consumer.getTopics();
                }

                if (command.equals("subscribe")) {

                    consumer.subscribe();
                }

                if (command.equals("quit")) {

                    consumer.close();
                    ok = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}

