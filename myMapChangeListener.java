import javafx.collections.MapChangeListener;

public class myMapChangeListener implements MapChangeListener {
    @Override
    public void onChanged(Change change) {
       String topicName = (String) change.getValueAdded();
       String consumerName = (String)change.getKey();
       System.out.println(consumerName + " subscribes to "+ topicName);
    }
}
