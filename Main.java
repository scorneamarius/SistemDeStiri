import javax.jms.JMSException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;
import javafx.collections.ObservableMap;
import javafx.collections.FXCollections;

public class Main {

    public static void main(String[] args) throws URISyntaxException, JMSException {

        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        List<Runnable> taskList = new ArrayList<Runnable>();
        Map<String,String> map = new HashMap<String,String>();
        ObservableMap<String,String> observableMap = FXCollections.observableMap(map);
        observableMap.addListener(new myMapChangeListener());

        String topicName1="Topic1";
        String topicName2="Topic2";


        Consumer c1 = new Consumer("Subscriber1",topicName1, observableMap);
        Consumer c2 = new Consumer("Subscriber2",topicName2, observableMap);
        Consumer c3 = new Consumer("Subscriber3",topicName1, observableMap);
        Producer p1 = new Producer("Producer1",new News(topicName1,"Text from Producer1"), observableMap);
        Producer p2 = new Producer("Producer2",new News(topicName2,"Text from Producer2"), observableMap);


        taskList.add(c1);
        taskList.add(c2);
        taskList.add(c3);
        taskList.add(p1);
        taskList.add(p2);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        for(int i=0;i<4;i++){
            Runnable currentTask = taskList.get(i);
            service.schedule(currentTask,i,TimeUnit.SECONDS);
        }

        System.out.println("Number of subscribers from topic " + topicName1 + ": " +p1.getNumberOfSubscribers(topicName1));
        System.out.println("Number of subscribers from topic " + topicName2 + ": " +p2.getNumberOfSubscribers(topicName2));

        service.shutdown();
        try {
            service.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
