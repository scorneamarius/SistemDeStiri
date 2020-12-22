package com.company;

import javax.jms.JMSException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException, JMSException {

        Consumer c1 = new Consumer("Subscriber1","topic1","destination1");
        Consumer c2 = new Consumer("Subscriber2","topic2","destination2");
    }
}
