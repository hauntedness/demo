package org.utils;

import java.io.Serializable;

public class Tuple2<T1, T2> implements Serializable {
    final private T1 t1;
    final private T2 t2;

    public Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public Tuple2<T2, T1> swap() {
        return new Tuple2<>(t2, t1);
    }

    public T1 _1() {
        return this.t1;
    }

    public T2 _2() {
        return this.t2;
    }

    public String toString() {

        return "(" + (this.t1 == null ? "" : this.t1.toString()) + "," + (this.t2 == null ? "" : this.t2.toString()) + ")";
    }
}
