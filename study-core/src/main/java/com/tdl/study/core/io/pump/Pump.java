package com.tdl.study.core.io.pump;

import com.tdl.study.core.io.input.Input;
import com.tdl.study.core.io.Openable;
import com.tdl.study.core.io.output.Output;

public interface Pump<V> extends Openable {
    Pump<V> batch(long batch);

    @Override
    default void open() {
        Openable.super.open();
//        Systems.handleSignal()

    }

    public static <V> Pump<V> pump(Input<V> input, int parallelism, Output<V> dst) {
        return new BasicPump<V>(input, parallelism, dst);
    }
}
