package test.alem.java_samples.serialize;

import alem.java_samples.serialization.Serializer;
import java.io.Serializable;
import org.junit.Test;

class Element implements Serializable {
    String text;
    double number;
}

public class TestSerialize {
    
    @Test
    public void testSerializable() {
        String filename = "element.test";
        
        Element e = new Element();
        e.text = "text";
        e.number = 1.1;

        Serializer.serialize(filename, e);
        Element ee = Serializer.deserialize(filename);

        assert (ee.text.equals("text"));
    }
}
