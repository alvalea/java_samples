package alem.java_samples.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations are <b>only</b> metadata and they do not contain any business
 * logic.
 * <br>
 * An annotation consumer is the piece of code that <b>reads</b> this metadata
 * and then performs necessary logic.
 * <br>
 * When we are talking about <b>standard</b> annotations, like @Override, the
 * JVM is the consumer and it works at bytecode level.
  */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Todo {

    public enum Priority {

        LOW, MEDIUM, HIGH
    }

    String author() default "alem";

    Priority priority() default Priority.LOW;
}
