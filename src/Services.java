import java.util.Random;

public class Services {

    private static final Random RANDOM = new Random();

    public static int generateId() {
        return RANDOM.nextInt(1, 100001);
    }

}
