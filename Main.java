import javax.jms.JMSException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import javafx.collections.ObservableMap;
import javafx.collections.MapChangeListener;
import javafx.collections.FXCollections;

public class Main {

    public static void main(String[] args) throws URISyntaxException, JMSException {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

        Map<String,String> map = new HashMap<String,String>();
        ObservableMap<String,String> observableMap = FXCollections.observableMap(map);
        observableMap.addListener(new MapChangeListener() {
            @Override
            public void onChanged(MapChangeListener.Change change) {
                System.out.println("Detected a change! ");
            }
        });

        Consumer c1 = new Consumer("Alfred","Topic1",observableMap );
        Consumer c2 = new Consumer("Alfred111","Topic2", observableMap);

        Thread t2 = new Thread(c1);
        Thread t4 = new Thread(c2);
        Thread t1 = new Thread(new Producer("Nume1", new News("Topic1","Autor1","Text")));
        Thread t3 = new Thread(new Producer("Nume2", new News("Topic2","Autor2","Text2")));

        t1.start();
        t3.start();
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        t4.start();

    }
}
