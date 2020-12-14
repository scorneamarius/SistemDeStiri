import javax.jms.JMSException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

import javafx.collections.ObservableMap;
import javafx.collections.MapChangeListener;
import javafx.collections.FXCollections;

public class Main {

    public static void main(String[] args) throws URISyntaxException, JMSException {

        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        List<Runnable> taskList = new ArrayList<Runnable>();
        Map<String,String> map = new HashMap<String,String>();
        ObservableMap<String,String> observableMap = FXCollections.observableMap(map);
        observableMap.addListener(new myMapChangeListener());

        Consumer c1 = new Consumer("Subscriber1","Topic1",observableMap );
        Consumer c2 = new Consumer("Subscriber2","Topic2", observableMap);
        Producer p1 = new Producer("Producer1",new News("Topic1","Text from Producer1"));
        Producer p2 = new Producer("Producer2",new News("Topic2","Text from Producer2"));

        taskList.add(c1);
        taskList.add(c2);
        taskList.add(p1);
        taskList.add(p2);


        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        for(int i=0;i<4;i++){
            Runnable currentTask = taskList.get(i);
            service.schedule(currentTask,i,TimeUnit.SECONDS);
        }

        service.shutdown();
        try {
            service.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
