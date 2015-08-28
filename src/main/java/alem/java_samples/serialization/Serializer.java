package alem.java_samples.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    public static <T> void serialize(String filename, T o) {
        try (FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);) {
            out.writeObject(o);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static <T> T deserialize(String filename) {
        T result = null;
        
        try (FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fis);) {
            result = (T) in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
}
