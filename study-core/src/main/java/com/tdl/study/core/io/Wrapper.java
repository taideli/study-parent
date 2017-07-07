package com.tdl.study.core.io;

import java.util.stream.Stream;

public interface Wrapper {

    static <T> WrapOutput<T> wrap(Output<?> base, Enqueue<T> d) {
        return new WrapOutput<T>(base) {
            @Override
            public long enqueue(Stream<T> items) {
                return d.enqueue(items);
            }
        };
    }

    abstract class WrapInput<V> implements Input<V> {

    }

    abstract class WrapOutput<V> implements Output<V> {
        private final Output<?> base;

        public WrapOutput(Output<?> origin) {
            this.base = origin;
        }

        @Override
        public long size() { return base.size();}

        @Override
        public long capacity() {return base.capacity();}

        @Override
        public boolean empty() {return base.empty();}

        @Override
        public boolean full() {return base.full();}

        @Override
        public String name() {return base.name();}

        // FIXME: 2017/7/7 implement openable interface's methods here
    }
}
