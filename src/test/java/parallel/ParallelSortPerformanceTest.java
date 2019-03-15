package parallel;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class ParallelSortPerformanceTest {

    private static final long LENGTH = 10000000L;

    private Double[] generateRandomArray(long size) {
        return Stream.generate(() -> new Random().nextDouble())
                .limit(size)
                .toArray(Double[]::new);
    }

    @Test
    public void testSerialSortPerformance() {
        Double[] randomDoubles = generateRandomArray(LENGTH);
        System.out.println(LocalDateTime.now() + ": START serial sort");
        Instant start = Instant.now();
        Arrays.sort(randomDoubles);
        Instant end = Instant.now();
        System.out.println(LocalDateTime.now() + ": END serial sort");
        System.out.println("Time taken = " + Duration.between(start, end).toMillis() + "ms");
    }

    @Test
    public void testParallelSortPerformance() {
        Double[] randomDoubles = generateRandomArray(LENGTH);
        System.out.println(LocalDateTime.now() + ": START parallel sort");
        Instant start = Instant.now();
        Arrays.parallelSort(randomDoubles);
        Instant end = Instant.now();
        System.out.println(LocalDateTime.now() + ": END parallel sort");
        System.out.println("Time taken = " + Duration.between(start, end).toMillis() + "ms");
    }

}
