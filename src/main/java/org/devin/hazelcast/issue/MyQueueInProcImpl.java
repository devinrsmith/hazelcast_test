package org.devin.hazelcast.issue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueueInProcImpl<T> implements MyQueue<T> {
    private final MyQueueBase<T> delegate;

    public MyQueueInProcImpl() {
        final Lock lock = new ReentrantLock(false);
        final Queue<T> queue = new LinkedList<>();
        delegate = new MyQueueBase<>(
                lock,
                lock.newCondition(),
                queue);
    }

    public void enqueue(@Nonnull final T t) {
        delegate.enqueue(t);
    }

    @Override
    @Nullable
    public T dequeue(final int timeout, final TimeUnit unit) throws InterruptedException {
        return delegate.dequeue(timeout, unit);
    }

}
