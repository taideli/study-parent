/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.util;

public class Tuple2<E1, E2> {
    private E1 first;
    private E2 second;

    public Tuple2(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E1 _1() {
        return first;
    }

    public E2 _2() {
        return second;
    }
}
