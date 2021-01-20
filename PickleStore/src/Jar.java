import java.util.UUID;

public class Jar {

    private Pickle[] pickles;
    private Integer nrOfPicklesInJar = 0;
    private boolean isCleaned = false;
    private boolean isReady = false;

    public Jar() {
        pickles = new Pickle[5];
    }

    void clean() {
        this.isCleaned = true;
    }

    public boolean isCleaned() {
        return isCleaned;
    }

    public boolean isReady() {
        return isReady;
    }

    boolean isEmpty() {
        if(nrOfPicklesInJar == 0){
            return true;
        }
        return false;
    }

    public void putPickleToJar(Pickle pickle) {
        if(!isReady){
            pickles[nrOfPicklesInJar] = pickle;
            nrOfPicklesInJar++;
            if(nrOfPicklesInJar == 5){
                pack();
            }
        }
        else{
            throw new IllegalArgumentException("Jar is already ready");
        }
    }

    void pack(){
        this.isReady = true;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return String.format("Jar{id= %s}", this.getId());
    }
}
