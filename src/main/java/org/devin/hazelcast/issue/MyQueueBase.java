package org.devin.hazelcast.issue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyQueueBase<T> implements MyQueue<T> {

    private final Lock lock;
    private final Condition queueNotify;
    private final Queue<T> queue;

    protected MyQueueBase(Lock lock, Condition queueNotify, Queue<T> queue) {
        this.lock = lock;
        this.queueNotify = queueNotify;
        this.queue = queue;
    }

    @Override
    public void enqueue(@Nonnull T t) {
        lock.lock();
        try {
            doAssert(queue.add(t), "queue.add(t)");
            queueNotify.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Nullable
    public T dequeue(int timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            T t = queue.poll();
            if (t == null) {
                queueNotify.await(timeout, unit);
                t = queue.poll();
            }
            return t;
        } finally {
            lock.unlock();
        }
    }

    private void doAssert(boolean b, String m) {
        if (!b) {
            throw new RuntimeException(m);
        }
    }
}
