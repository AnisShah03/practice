package multithread;

public class CounterTest {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        Runnable task = () -> {
            for (int i = 0; i < 10_000_000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        // start stopwatch
        long startTime = System.nanoTime();

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // stop stopwatch
        long endTime = System.nanoTime();

        long durationInMillis = (endTime - startTime) / 1_000_000;

        System.out.println("Final count: " + counter.getCount());
        System.out.println("Time taken: " + durationInMillis + " ms");
    }
}
