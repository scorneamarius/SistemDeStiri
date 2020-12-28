import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestClass {
    public static void main(String [] argv)
    {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        List<Runnable> taskList = new ArrayList<Runnable>();

        News news1 = new News("Vreme","Astazi ninge","publisher1");
        News news2 = new News("fotbal","FCSB e pe primul loc in clasament","publisher2");


        Runnable publisher1 = new Producer(news1,news1.getDomain(),"rxTopicPublisher1");
        Runnable publisher2 = new Producer(news2,news2.getDomain(),"rxTopicPublisher2");

        Runnable reader1 = new Consumer("vreme","rxTopicPublisher1");
        Runnable reader2 = new Consumer("fotbal","rxTopicPublisher2");

        taskList.add(reader1);
        taskList.add(reader2);
        taskList.add(publisher1);
        taskList.add(publisher2);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        for(int i=0;i<4;i++){
            Runnable currentTask = taskList.get(i);
            service.schedule(currentTask,i, TimeUnit.SECONDS);
        }

        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
        try {
            service.awaitTermination(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
