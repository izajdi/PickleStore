import java.util.Objects;
import java.util.UUID;

public class Pickle {

    private boolean isCleaned = false;
    private boolean isPacked = false;
    private final PickleType pickleType;

    public Pickle(PickleType pickleType) {
        this.pickleType = pickleType;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }

    void clean() {
        this.isCleaned = true;
    }

    void pack() {
        this.isPacked = true;
    }

    public PickleType getPickleType() {
        return pickleType;
    }

    @Override
    public String toString() {
        return String.format("Pickle{id= %s, pickleType= %s}", this.getId(), pickleType);
    }
}
