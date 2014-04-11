package org.devin.hazelcast.issue;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICondition;
import com.hazelcast.core.ILock;

import java.util.concurrent.TimeUnit;

/**
 * Created by devin on 4/11/14.
 */
public class SpamLockCondition extends Thread {
    private final HazelcastInstance hazel;

    public SpamLockCondition(HazelcastInstance hazel) {
        this.hazel = hazel;
    }

    @Override
    public void run() {
        final ILock lock = hazel.getLock("myLock");
        final ICondition condition = lock.newCondition("myCondition");
        while (!isInterrupted()) {
            lock.lock();
            try {
                try {
                    condition.await(1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    //
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final Config config = new Config();
        final HazelcastInstance hazel = Hazelcast.newHazelcastInstance(config);
        final int numThreads = 4;
        final Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            threads[i] = new SpamLockCondition(hazel);
        }
        for (int i = 0; i < numThreads; ++i) {
            threads[i].start();
        }
    }
}
