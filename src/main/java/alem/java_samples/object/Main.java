package alem.java_samples.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class MyObject implements Cloneable {

    private String text;
    private List<Integer> numbers;

    public MyObject(String _text, int... _numbers) {
        text = _text;
        
        numbers = new ArrayList<>();
        for(int number : _numbers) {
            numbers.add(number);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MyObject[");

        sb.append("text=");
        sb.append(text);

        sb.append(", numbers=");
        sb.append(numbers.toString());

        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if ((obj != null) && (obj instanceof MyObject)) {
            MyObject other = (MyObject) obj;
            if (this.text.equals(other.text)
                    && this.numbers.equals(other.numbers)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.text);
        hash = 47 * hash + Objects.hashCode(this.numbers);
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        MyObject result = (MyObject) (super.clone());
        result.text = this.text;
        
        result.numbers = new ArrayList<>();
        for (Integer number : numbers) {
            result.numbers.add(number);
        }
        
        return result;
    }

}

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        MyObject myObject = new MyObject("h1", 1,2,3);
        System.out.println(myObject);
        
        MyObject myObject2 = (MyObject)myObject.clone();
        System.out.println(myObject2);
        
        HashMap<MyObject, String> map = new HashMap<MyObject, String>();
        map.put(myObject, "myObject");
        map.put(myObject2, "myObject2");
        
        System.out.println("map.size(): "+map.size());
        System.out.println(map);
    }
}
