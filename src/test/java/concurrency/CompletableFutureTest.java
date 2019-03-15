package concurrency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CompletableFutureTest {

    private static ExecutorService service = Executors.newCachedThreadPool();

    @Test
    @DisplayName("Already Completed CompletableFuture")
    public void testAlreadyCompletedCompletableFuture() throws Exception {
        String expectedValue = "blah";
        CompletableFuture<String> alreadyCompleted = CompletableFuture.completedFuture(expectedValue);
        assertEquals(expectedValue, alreadyCompleted.get());
    }

    @Test
    @DisplayName("Run Async Task")
    public void testRunAsync() throws Exception {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName() + ": running async task"), service);
        Thread.sleep(2000);
        assertTrue(runAsync.isDone());
    }

    @Test
    @DisplayName("Run Async Task (via Supplier)")
    public void testSupplyAsync() throws Exception {
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "Final Result", service);
        Thread.sleep(2000);
        assertTrue(supplyAsync.isDone());
        assertEquals("Final Result", supplyAsync.get());
    }

    @Test
    @DisplayName("Run task based on CompletableFuture success")
    public void testChainingCompletableFutures() throws Exception {
        Map<String, String> cache = new HashMap<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        assertEquals(2, cache.size());

        CompletableFuture<String> lookupFuture = CompletableFuture.supplyAsync(() -> cache.get("key1"), service);
        lookupFuture.thenRunAsync(cache::clear, service);

        Thread.sleep(2000);

        assertEquals("value1", lookupFuture.get());
        assertTrue(cache.isEmpty());
        assertTrue(lookupFuture.isDone());
    }

    @Test
    @DisplayName("Composing Tasks")
    public void testComposing() throws Exception {
        Function<Integer, Supplier<List<Integer>>> getFirstTenMultiples = num -> () -> Stream.iterate(num, i -> i + num).limit(10).collect(Collectors.toList());

        Supplier<List<Integer>> multiplesSupplier = getFirstTenMultiples.apply(13);

        // Original CompletionFuture
        CompletableFuture<List<Integer>> getMultiples = CompletableFuture.supplyAsync(multiplesSupplier, service);

        // Function that takes input from original CompletionStage
        Function<List<Integer>, CompletableFuture<Integer>> sumNumbers = multiples -> CompletableFuture.supplyAsync(() -> multiples.stream().mapToInt(Integer::intValue).sum());

        //The final CompletableFuture composed of previous two
        CompletableFuture<Integer> summedMultiples = getMultiples.thenComposeAsync(sumNumbers, service);
        assertEquals(715, summedMultiples.get().intValue());
    }

    @Test
    @DisplayName("Combining Tasks")
    public void testCombining() throws Exception {
        CompletableFuture<String> firstTask = CompletableFuture.supplyAsync(() -> "this is");
        CompletableFuture<String> secondTask = CompletableFuture.supplyAsync(() -> "the whole thing");
        CompletableFuture<String> wholeTask = firstTask.thenCombineAsync(secondTask, (f, s) -> f + " " + s, service);

        assertEquals("this is the whole thing", wholeTask.get());
    }

}
