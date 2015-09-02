package alem.java_samples.api.service.internal;

import alem.java_samples.api.service.IMyService;

public class MyService implements IMyService{

    private final MyServiceImpl impl;
    
    public MyService() {
        impl = new MyServiceImpl();
    }
    
    @Override
    public void process() {
        impl.process();
    }

}
