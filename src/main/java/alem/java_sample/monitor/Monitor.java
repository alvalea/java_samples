package alem.java_sample.monitor;

import java.util.logging.Level;
import java.util.logging.Logger;

class SafeQueue {

    final int SIZE = 10;

    String[] queue = new String[SIZE];
    int size = 0;
    int head = 0;
    int tail = 0;

    synchronized void put(String s) throws InterruptedException {
        while (size == SIZE) {
            wait();
        }
        queue[head] = s;
        head = (head + 1) % SIZE;
        size++;

        notifyAll();
    }

    synchronized String get() throws InterruptedException {
        String result = "";
        while (size == 0) {
            wait();
        }
        result = queue[tail];
        tail = (tail + 1) % SIZE;
        size--;

        notifyAll();

        return result;
    }
}

public class Monitor {

    static void producer(SafeQueue queue) {
        new Thread(() -> {
            for (int i = 0; i < 20; ++i) {
                try {
                    queue.put(String.valueOf(i));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    static void consumer(SafeQueue queue) {
        new Thread(() -> {
            for (int i = 0; i < 20; ++i) {
                try {
                    System.out.println(queue.get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        SafeQueue queue = new SafeQueue();
        
        consumer(queue);
        producer(queue);
    }
}
