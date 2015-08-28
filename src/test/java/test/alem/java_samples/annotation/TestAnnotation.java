package test.alem.java_samples.annotation;

import alem.java_samples.annotation.Todo;
import alem.java_samples.annotation.TodoAnnotation;
import java.lang.reflect.Method;
import org.junit.Test;

public class TestAnnotation {

    @Test
    public void testAnnotation() {
        for (Method method : TodoAnnotation.class.getMethods()) {
            Todo todo = (Todo) method.getAnnotation(Todo.class);
            if (todo != null) {
                assert(todo.priority() == Todo.Priority.MEDIUM);
            }
        }
    }
}
