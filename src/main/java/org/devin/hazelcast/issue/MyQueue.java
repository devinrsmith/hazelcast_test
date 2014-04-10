package org.devin.hazelcast.issue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * User: dsmith
 * Date: 4/10/14
 */
public interface MyQueue<T> {
    void enqueue(@Nonnull T t);

    @Nullable
    T dequeue(int timeout, TimeUnit unit) throws InterruptedException;
}
