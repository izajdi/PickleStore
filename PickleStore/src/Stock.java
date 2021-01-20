import java.time.Duration;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Stock {

    private final String cleanPickleThreadName = "CLEAN PICKLE THREAD";
    private final String cleanJarThreadName = "CLEAN JAR THREAD";
    private final String prepareJarCezarThreadName = "PREPARE CEZAR JAR THREAD";
    private final String prepareJarIzydThreadName = "PREPARE IZYD JAR THREAD";

    private static final Random generator = new Random();

    private int picklesLeftToPrepare = 0;
    private Queue<Pickle> uncleanedPickles;
    private Queue<Pickle> cleanedPickles = new ConcurrentLinkedDeque<>();
    private Queue<Jar> uncleanedJars;
    private Queue<Jar> cleanedJars = new ConcurrentLinkedDeque<>();
    private Queue<Jar> readyJars = new ConcurrentLinkedDeque<>();

    public Stock(Queue<Pickle> pickles, Queue<Jar> jars) {
        this.uncleanedPickles = pickles;
        this.uncleanedJars = jars;
        this.picklesLeftToPrepare = uncleanedPickles.size();
    }

    private int getNumberOfUncleanedPickles() {
        return uncleanedPickles.size();
    }

    private int getNumberOfCleanedPickles() {
        return cleanedPickles.size();
    }

    private int getNumberOfPicklesToCleaned() {
        return getNumberOfUncleanedPickles() - getNumberOfCleanedPickles();
    }

    private int getNumberOfUncleanedJars() {
        return uncleanedJars.size();
    }

    private int getNumberOfCleanedJars() {
        return cleanedJars.size();
    }

    private int getNumberOfJarsToCleaned() {
        return getNumberOfUncleanedJars() - getNumberOfCleanedJars();
    }

    private int getNumberOfReadyJars() {
        return readyJars.size();
    }

    private String getMessageAfterClean(Thread thread, Object object) {
        String objectMessage = String.format("%s was cleaned", object.toString());
        return String.format("[THREAD ID = %d][THREAD NAME = %s]: %s", thread.getId(), thread.getName(), objectMessage);
    }

    private String getMessageBeforePreparingJar(Thread thread, PickleType pickleType) {
        String message = String.format("%s jar is starting to prepare", pickleType);
        return String.format("[THREAD ID = %d][THREAD NAME = %s]: %s", thread.getId(), thread.getName(), message);
    }

    private String getMessageAfterPackedPickle(Thread thread, Pickle pickle, Jar jar) {
        String message = String.format("%s was packed to %s", pickle.toString(), jar.toString());
        return String.format("[THREAD ID = %d][THREAD NAME = %s]: %s", thread.getId(), thread.getName(), message);
    }

    private String getMessageForFullJar(Thread thread, Jar jar, PickleType pickleType) {
        String message = String.format("%s is ready and full of %s pickles", jar.toString(), pickleType);
        return String.format("[THREAD ID = %d][THREAD NAME = %s]: %s", thread.getId(), thread.getName(), message);
    }

    Thread cleanPickleThread = new Thread(() -> {
        this.cleanPickleThread.setName(cleanPickleThreadName);
        while (!uncleanedPickles.isEmpty()) {
            try {
                Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Pickle currentPickle = uncleanedPickles.poll();
            currentPickle.clean();
            String message = getMessageAfterClean(this.cleanPickleThread, currentPickle);
            System.out.println(message);
            cleanedPickles.add(currentPickle);
        }
    });

    Thread cleanJarThread = new Thread(() -> {
        this.cleanJarThread.setName(cleanJarThreadName);
        while (!uncleanedJars.isEmpty()) {
            try {
                Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Jar currentJar = uncleanedJars.poll();
            currentJar.clean();
            String message = getMessageAfterClean(this.cleanJarThread, currentJar);
            System.out.println(message);
            cleanedJars.add(currentJar);
        }
    });

    Thread prepareIzydJarThread = new Thread(() -> {
        this.prepareIzydJarThread.setName(prepareJarIzydThreadName);
        System.out.println(getMessageBeforePreparingJar(this.prepareIzydJarThread, PickleType.IZYD));
        while (picklesLeftToPrepare != 0) {
            try {
                Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Jar currentJar = this.getCleanedJar();
            if (currentJar != null) {
                while (!currentJar.isReady()) {
                    try {
                        Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Pickle currentPickle = getProperPickle(PickleType.IZYD);
                    if (currentPickle != null) {
                        processPickleInJar(currentJar, currentPickle);
                        System.out.println(
                                getMessageAfterPackedPickle(this.prepareIzydJarThread, currentPickle, currentJar));
                        picklesLeftToPrepare--;
                    }
                }
                readyJars.add(currentJar);
                System.out.println(getMessageForFullJar(this.prepareIzydJarThread, currentJar, PickleType.IZYD));
            }
        }
    });

    Thread prepareCezarJarThread = new Thread(() -> {
        this.prepareCezarJarThread.setName(prepareJarCezarThreadName);
        System.out.println(getMessageBeforePreparingJar(this.prepareCezarJarThread, PickleType.CEZAR));
        while (picklesLeftToPrepare != 0) {
            try {
                Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Jar currentJar = this.getCleanedJar();
            if (currentJar != null) {
                while (!currentJar.isReady()) {
                    try {
                        Thread.sleep(Duration.ofSeconds(generator.nextInt(5)).toMillis());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Pickle currentPickle = getProperPickle(PickleType.CEZAR);
                    if (currentPickle != null) {
                        processPickleInJar(currentJar, currentPickle);
                        System.out.println(
                                getMessageAfterPackedPickle(this.prepareCezarJarThread, currentPickle, currentJar));
                        picklesLeftToPrepare--;
                    }
                }
                readyJars.add(currentJar);
                System.out.println(getMessageForFullJar(this.prepareCezarJarThread, currentJar, PickleType.CEZAR));
            }
        }
    });


    Jar getCleanedJar() {
        if (!cleanedJars.isEmpty()) {
            return cleanedJars.poll();
        }
        return null;
    }

    Pickle getProperPickle(PickleType pickleType) {
        if (!cleanedPickles.isEmpty() && cleanedPickles.peek().getPickleType().equals(pickleType)) {
            return cleanedPickles.poll();
        }
        return null;
    }

    void processPickleInJar(Jar jar, Pickle pickle) {
        jar.putPickleToJar(pickle);
        pickle.pack();
    }
}
