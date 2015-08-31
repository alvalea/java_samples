package alem.java_samples.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Stream {

    static void log(String taskName, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        double msecs = (end - start) / 1000;
        Logger.getLogger(Stream.class.getName()).log(Level.INFO,
                "[{0}] msecs: {1}", new Object[]{taskName, msecs});
    }

    public static void main(String[] args) {
        List<String> myList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000000; i++) {
            sb.delete(0, sb.length());
            sb.append("o");
            sb.append(String.valueOf(i));
            myList.add(sb.toString());
        }

        log("Sequential", () -> {
            myList.stream().
                    filter(i -> i.startsWith("o")).
                    collect(Collectors.toList());
        });

        log("Parallel", () -> {
            myList.stream().parallel().
                    filter(i -> i.startsWith("o")).
                    collect(Collectors.toList());
        });
    }
}
