import java.util.Random;

public class LocationTimeGenerator {
    private static final String[] LOCATIONS = {
            "Johor, Skudai Hall",
            "Johor, Gelang Patah Hall",
            "Kuala Lumpur, KL Convention Center",
            "Penang, Georgetown Hall",
            "Selangor, Shah Alam Stadium"
    };
    private static final String[] TIMES = {
            "December 20, 2024, 10:00 AM",
            "December 21, 2024, 2:00 PM",
            "December 22, 2024, 11:00 AM",
            "December 23, 2024, 1:00 PM",
            "December 24, 2024, 4:00 PM"
    };
    private static final Random RANDOM = new Random();

    public static String generateRandomLocationTime() {
        String location = LOCATIONS[RANDOM.nextInt(LOCATIONS.length)];
        String time = TIMES[RANDOM.nextInt(TIMES.length)];
        return location + " / " + time;
    }
}
