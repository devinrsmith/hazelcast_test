package org.devin.hazelcast.issue;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class MyQueueHazelcastImpl<T> implements MyQueue<T> {
    private static final String UNIQUE = "aoesnhao";

    private final MyQueueBase<T> delegate;

    public MyQueueHazelcastImpl(String name, HazelcastInstance hz) {
        final ILock lock = hz.getLock(name + ".lock." + UNIQUE);
        final Condition queueNotify = lock.newCondition(name + ".queueNotify." + UNIQUE);
        final Queue<T> queue = hz.getQueue(name + ".queue." + UNIQUE);
        delegate = new MyQueueBase<>(
                lock,
                queueNotify,
                queue);
    }

    @Override
    public void enqueue(@NonNull final T t) {
        delegate.enqueue(t);
    }

    @Override
    @Nullable
    public T dequeue(final int timeout, final TimeUnit unit) throws InterruptedException {
        return delegate.dequeue(timeout, unit);
    }
}
