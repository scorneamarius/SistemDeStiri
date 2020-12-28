import javafx.collections.MapChangeListener;

public class myMapChangeListener implements MapChangeListener {
    @Override
    public void onChanged(Change change) {
       Integer contor = (Integer) change.getValueAdded();
       News news  = (News)change.getKey();
       System.out.println("News: " + news + "Domain: " + news.getDomain() + "has been read for" + contor + "times.");
    }
}
