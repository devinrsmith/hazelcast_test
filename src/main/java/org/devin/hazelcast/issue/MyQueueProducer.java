package org.devin.hazelcast.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * User: dsmith
 * Date: 4/10/14
 */
public class MyQueueProducer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MyQueueProducer.class);

    private final Random r;
    private final MyQueue<Integer> queue;

    private final int minSleep;
    private final int maxSleep;

    private final int minEnqueue;
    private final int maxEnqueue;

    public MyQueueProducer(final MyQueue<Integer> queue, final int minSleep, final int maxSleep, final int minEnqueue, final int maxEnqueue) {
        r = new Random();
        this.queue = queue;
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
        this.minEnqueue = minEnqueue;
        this.maxEnqueue = maxEnqueue;
    }

    @Override
    public void run() {
        final Thread t = Thread.currentThread();
        while (!t.isInterrupted()) {
            final int producing = getRandom(minEnqueue, maxEnqueue);
            log.info("Producing {}", producing);
            queue.enqueue(producing);
            try {
                Thread.sleep(getRandom(minSleep, maxSleep));
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private int getRandom(int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }
}
