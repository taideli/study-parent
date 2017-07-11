package com.tdl.study.core.io.pump;

import com.tdl.study.core.base.Namedly;
import com.tdl.study.core.io.Openable;
import com.tdl.study.core.io.OpenableThread;
import com.tdl.study.core.lambda.Runnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class PumpImpl<V, P extends PumpImpl<V, P>> extends Namedly implements Pump<V> {

    protected final String name;
    private final int parallelism;
    private final List<OpenableThread> tasks = new ArrayList<>();
    protected long batchSize = 1000;
    protected final List<AutoCloseable> dependencies;
    protected long forceTrace; // how many items when write a log

    protected PumpImpl(String name, int parallelism) {
        super(name);
        this.name = name;
        if (parallelism < 0) this.parallelism = 1;
        else if (parallelism == 0) this.parallelism = 16;
        else this.parallelism = parallelism;
        forceTrace = batchSize / parallelism;
        dependencies = new ArrayList<>();
        logger().info("Pump [" + name + "] created with parallelism [" + this.parallelism + "]");
    }

    @Override
    public final PumpImpl<V, P> batch(long batch) {
        this.batchSize = batch;
        this.forceTrace = batch / parallelism;
        return this;
    }

    protected final void  dependencies(List<? extends AutoCloseable> deps) {
        this.dependencies.addAll(deps);
    }

    protected final void dependencies(AutoCloseable... deps) {
        dependencies(Arrays.asList(deps));
    }

    private void closeDependencies() {
        for (AutoCloseable dep : dependencies) {
            try {
                dep.close();
            } catch (Exception e) {
                logger().error(dep.getClass().getName() + " close failed");
            }
        }
    }

    protected final void pumping(Supplier<Boolean> sourceEmpty, Runnable pumping) {
        Runnable r = Runnable
                .exception(pumping, ex -> logger().error("Pump processing failure", ex))
                .until(() -> !opened() || sourceEmpty.get());
        for (int i = 0; i < parallelism; i++)
            tasks.add(new OpenableThread(r, name() + "Pump#" + i));
    }

    @Override
    public void open() {
        Pump.super.open();
        for (OpenableThread t : tasks) t.open();
        try {
            for (OpenableThread t : tasks)  try {
                t.join();
            } catch (InterruptedException e) {
                t.close();
            }
        } finally {
            close();
        }
        logger().info(name() + " finished.");
    }

    protected boolean isAllDependenciesOpened() {
        return dependencies.stream()
                .map(c -> !(c instanceof Openable) || ((Openable) c).opened())
                .reduce((r1, r2) -> r1 && r2)
                .orElse(true);
    }

    @Override
    public boolean opened() {
        return Pump.super.opened() && isAllDependenciesOpened();
    }

    @Override
    public void close() {
        Pump.super.close();
        closeDependencies();
    }
}
