package com.company;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class Producer {

    private String name;
    private TopicConnection connection;
    private TopicSession session;
   // private boolean ok=true;

    public Producer(String name){
        this.name=name;
        connect();
    }

    public void connect(){
        try {
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
            TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createTopicConnection();

            session=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close(){

            try {
                if(connection!=null) {
                    connection.stop();
                    session.close();
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

    }
    public String getName(){
        return name;
    }

    public void addNews() throws JMSException,IOException{
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Set news domain: ");
        String domain = keyboard.readLine();
        System.out.print("Title: ");
        String title = keyboard.readLine();
        System.out.print("Text: ");
        String text = keyboard.readLine();

        News theNews = new News(domain,title,this.getName(),text);

        Destination destination = session.createTopic(domain);
        MessageProducer producer = session.createProducer(destination);

        ObjectMessage message = session.createObjectMessage();
        message.setObject(theNews);
        producer.send(message);
    }

    public void modifyNews(){

    }

    public void deleteNews(){

    }

    public void getNumberOfReaders(){

    }


    public static void main(String[] args)throws JMSException{

        System.out.print("Type your name: ");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String name = null;
        try {
            name = keyboard.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Producer producer=new Producer(name);
        boolean ok=true;

        System.out.print("Type menu");

        while(ok) {

            try {
                BufferedReader keyboard2 = new BufferedReader(new InputStreamReader(System.in));
                String command = null;
                command = keyboard2.readLine();

                if(command.equals("menu")){
                    System.out.print("-Add news\n");
                    System.out.print("-Modify news\n");
                    System.out.print("-Delete news\n");
                    System.out.print("-Get the number of readers\n");
                    System.out.print("-Quit\n");
                }

                if(command.equals("add news")){

                    producer.addNews();
                }
                if(command.equals("modify news")){

                    producer.modifyNews();
                }
                if(command.equals("delete news")){

                    producer.deleteNews();
                }
                if(command.equals("get number")){

                    producer.getNumberOfReaders();
                }
                if(command.equals("quit")){

                    producer.close();
                    ok=false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }


    }


}

