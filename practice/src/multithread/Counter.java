package multithread;

import java.util.concurrent.atomic.LongAdder;

class Counter {
    private LongAdder count = new LongAdder();

    public void increment() {
        count.increment();
    }

    public long getCount() {
        return count.sum();
    }
}
