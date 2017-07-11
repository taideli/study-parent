package com.tdl.study.core.io;

import com.tdl.study.core.io.input.Input;
import com.tdl.study.core.io.output.Enqueue;
import com.tdl.study.core.io.output.Output;
import com.tdl.study.core.lambda.Runnable;

import java.util.stream.Stream;

public interface Wrapper {

    static <T> WrapOutput<T> wrap(Output<?> base, final Enqueue<T> d) {
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

        public void opening(Runnable handler) {this.base.opening(handler);}

        public void closing(Runnable handler) {this.base.closing(handler);}

        public void open() {this.base.open();}

        public void close() {this.base.close();}

        public boolean opened() {return base.opened();}

        public boolean closed() {return base.closed();}
    }
}
