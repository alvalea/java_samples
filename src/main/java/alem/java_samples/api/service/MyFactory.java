package alem.java_samples.api.service;

import alem.java_samples.api.service.internal.MyService;

public class MyFactory {

    public static IMyService create() {
        return new MyService();
    }
}
