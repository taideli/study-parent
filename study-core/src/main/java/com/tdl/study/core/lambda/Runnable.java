package com.tdl.study.core.lambda;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Runnable extends java.lang.Runnable {
    @Override
    void run();

    default Runnable prior(java.lang.Runnable prior) {
        return () -> {
            prior.run();
            run();
        };
    }

    default Runnable then(java.lang.Runnable then) {
        return () -> {
            run();
            then.run();
        };
    }

    default Runnable until(Supplier<Boolean> stopping) {
        return () -> {
            while (!stopping.get()) this.run();
        };
    }

    default Runnable exception(Consumer<Exception> handler) {
        return () -> {
            try {
                run();
            } catch (Exception ex) {
                handler.accept(ex);
            }
        };
    }

    static Runnable merge(java.lang.Runnable... runs) {
        return () -> {
            for (java.lang.Runnable r : runs) {
                if (null != r) r.run();
            }
        };
    }

    static Runnable until(java.lang.Runnable run, Supplier<Boolean> stopping) {
        return () -> {
            while (!stopping.get())
                run.run();
        };
    }

    static Runnable exception(java.lang.Runnable run, Consumer<Exception> handler) {
        return () -> {
            try {
                run.run();
            } catch (Exception ex) {
                handler.accept(ex);
            }
        };
    }
}
