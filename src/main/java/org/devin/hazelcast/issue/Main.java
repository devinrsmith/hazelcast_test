package org.devin.hazelcast.issue;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * User: dsmith
 * Date: 4/10/14
 */
public class Main {

    public static void main(String[] args) {

        final MyQueue<Integer> queue = getHazelcastImpl();

//        final MyQueue<Integer> queue = getInProcImpl();

        final int numProducers = 1;
        final int numConsumers = 4;
        final int minSleep = 1;
        final int maxSleep = 1000;
        final int minEnqueue = 1000;
        final int maxEnqueue = 1001;

        startConsumers(queue, numConsumers);
        startProducers(queue, numProducers, minSleep, maxSleep, minEnqueue, maxEnqueue);
    }

    private static MyQueue<Integer> getInProcImpl() {
        return new MyQueueInProcImpl<>();
    }

    private static MyQueue<Integer> getHazelcastImpl() {
        final Config config = new Config();
        final HazelcastInstance hazel = Hazelcast.newHazelcastInstance(config);
        return new MyQueueHazelcastImpl<>("test", hazel);
    }

    private static void startProducers(final MyQueue<Integer> queue, final int numProducers, final int minSleep, final int maxSleep, final int minEnqueue, final int maxEnqueue) {
        final Thread[] producers = new Thread[numProducers];
        for (int i = 0; i < numProducers; ++i) {
            producers[i] = new Thread(new MyQueueProducer(queue, minSleep, maxSleep, minEnqueue, maxEnqueue));
            producers[i].setName("Producer-" + i);
        }

        for (int i = 0; i < numProducers; ++i) {
            producers[i].start();
        }
    }

    private static void startConsumers(final MyQueue<Integer> queue, final int numConsumers) {
        final Thread[] consumers = new Thread[numConsumers];
        for (int i = 0; i < numConsumers; ++i) {
            consumers[i] = new Thread(new MyQueueConsumer(queue));
            consumers[i].setName("Consumer-" + i);
        }
        for (int i = 0; i < numConsumers; ++i) {
            consumers[i].start();
        }
    }
}
