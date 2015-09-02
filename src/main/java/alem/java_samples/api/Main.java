package alem.java_samples.api;

import alem.java_samples.api.service.IMyService;
import alem.java_samples.api.service.MyFactory;

public class Main {

    public static void main(String[] args) {
        IMyService service = MyFactory.create();
        service.process();
    }
}
