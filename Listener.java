import javax.jms.*;

public class Listener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage =(ObjectMessage)message;
        try {
            News news = (News)objectMessage.getObject();
            if(news!=null){
                System.out.println("---------NEWS----------");
                System.out.println("News domain: "+news.getDomain());
                System.out.println("News author: "+news.getAuthor());
                System.out.println("News text: "+news.getText());
                System.out.println("Publication date: "+news.getPublicationDate());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}