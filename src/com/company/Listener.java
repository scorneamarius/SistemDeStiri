package com.company;

import javax.jms.*;

public class Listener implements MessageListener {

    private String consumerName;

    public Listener(String consumerName){
        this.consumerName=consumerName;
    }

    @Override
    public void onMessage(Message message) {


        ObjectMessage objectMessage =(ObjectMessage)message;
        try {
            News news = (News)objectMessage.getObject();
            System.out.println(consumerName+" receives a news");
            if(news!=null){
                System.out.println("News domain: "+news.getDomain());
                System.out.println("News author: "+news.getAuthor());
                System.out.println("News text: "+news.getText());
                System.out.println("Publication date: "+news.getPublicationDate());
               // System.out.println("\n");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}