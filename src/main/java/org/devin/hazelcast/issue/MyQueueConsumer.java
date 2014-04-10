package org.devin.hazelcast.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * User: dsmith
 * Date: 4/10/14
 */
public class MyQueueConsumer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MyQueueConsumer.class);
    private final MyQueue<Integer> queue;

    public MyQueueConsumer(final MyQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        final Thread t = Thread.currentThread();
        while (!t.isInterrupted()) {
            Integer work = null;
            try {
                work = queue.dequeue(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
            if (work != null) {
                log.info("Consuming {}", work);
                try {
                    Thread.sleep(work);
                } catch (InterruptedException e) {
                    // ignore
                }
            } else {
                log.info("Consuming nothing");
            }
        }
    }
}
