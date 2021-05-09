package javacom;

import java.io.Serializable;

@FunctionalInterface
public interface MyFunction<Integer, String> extends Serializable {
    String call(Integer value) throws Exception;
}
