package com.tdl.study.core.io.output;

import com.tdl.study.core.io.IO;
import com.tdl.study.core.io.Wrapper;
import com.tdl.study.core.parallel.Streams;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Output<V> extends IO, Consumer<Stream<V>>, Enqueue<V> {
    Output<?> NULL = items -> 0;

    @Override
    default long size() {return 0;}

    @Override
    default void accept(Stream<V> items) {enqueue(items);}

    default <V0> Output<V0> prior(Function<V0, V> conv) {
        return Wrapper.wrap(this, items -> enqueue(Streams.of(items.map(conv))));
    }

    static <T> Output<T> of(Collection<? super T> latent) {
        return items -> {
            items.forEach(latent::add);
            return latent.size();
        };
    }
}
