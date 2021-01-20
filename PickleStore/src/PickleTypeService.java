import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PickleTypeService {

    private final List<PickleType> VALUES = Collections.unmodifiableList(Arrays.asList(PickleType.values()));
    private final int SIZE = VALUES.size();
    private final Random RANDOM = new Random();

    public PickleType getRandomPickleType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
