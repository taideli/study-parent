package com.tdl.study.core.io;

import com.tdl.study.core.io.input.Dequeue;
import com.tdl.study.core.io.input.Input;
import com.tdl.study.core.io.output.Enqueue;
import com.tdl.study.core.io.output.Output;
import com.tdl.study.core.lambda.Runnable;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Wrapper {

    static <T> WrapInput<T> wrap(Input<?> base, final Dequeue<T> d) {
        return new WrapInput<T>(base) {
            @Override
            public long dequeue(Function<Stream<T>, Long> using, long batchSize) {
                return d.dequeue(using, batchSize);
            }
        };
    }

    static <T> WrapOutput<T> wrap(Output<?> base, final Enqueue<T> d) {
        return new WrapOutput<T>(base) {
            @Override
            public long enqueue(Stream<T> items) {
                return d.enqueue(items);
            }
        };
    }

    abstract class WrapInput<V> implements Input<V> {
        private final Input<?> base;

        public WrapInput(Input<?> origin) {
            this.base = origin;
        }

        /** This override is redundant because it's abstract already*/
/*
        @Override
        public abstract long dequeue(Function<Stream<V>, Long> using, long batchSize);
*/

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

        @Override
        public void opening(Runnable handler) {this.base.opening(handler);}

        @Override
        public void closing(Runnable handler) {this.base.closing(handler);}

        @Override
        public void open() {this.base.open();}

        @Override
        public void close() {this.base.close();}

        @Override
        public boolean opened() {return base.opened();}

        @Override
        public boolean closed() {return base.closed();}

        @Override
        public String toString() {return base.toString() + "WrapIn";}
    }

    abstract class WrapOutput<V> implements Output<V> {
        private final Output<?> base;

        public WrapOutput(Output<?> origin) {
            this.base = origin;
        }

        /** This override is redundant because it's abstract already*/
/*
        @Override
        public abstract long enqueue(Stream<V> items);
*/

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

        @Override
        public void opening(Runnable handler) {this.base.opening(handler);}

        @Override
        public void closing(Runnable handler) {this.base.closing(handler);}

        @Override
        public void open() {this.base.open();}

        @Override
        public void close() {this.base.close();}

        @Override
        public boolean opened() {return base.opened();}

        @Override
        public boolean closed() {return base.closed();}

        @Override
        public String toString() {return base.toString() + "WrapOut";}
    }
}
