import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        PickleTypeService pickleTypeService = new PickleTypeService();
        Queue<Pickle> pickles = new ConcurrentLinkedQueue<>();
        Queue<Jar> jars = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 20; i++) {
            pickles.add(new Pickle(pickleTypeService.getRandomPickleType()));
            jars.add(new Jar());
        }
        Stock stock = new Stock(pickles, jars);
        stock.cleanPickleThread.start();
        stock.cleanJarThread.start();
        stock.prepareCezarJarThread.start();
        stock.prepareIzydJarThread.start();

    }


}
