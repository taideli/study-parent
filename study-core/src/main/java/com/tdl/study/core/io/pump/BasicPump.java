package com.tdl.study.core.io.pump;

import com.tdl.study.core.io.input.Input;
import com.tdl.study.core.io.output.Output;
import com.tdl.study.core.parallel.Concurrents;

public class BasicPump<V> extends PumpImpl<V, BasicPump<V>> {

    public BasicPump(Input<V> input, int parallelism, Output<V> output) {
        super(input.name() + "->" + output.name(), parallelism);
        dependencies(input, output);
        logger().debug("Trace force on batch less than [" + forceTrace + "]");
        pumping(input::empty, () -> pumpOne(input, output));
    }

    private void pumpOne(Input<V> input, Output<V> output) {
        if (opened()) {
            long cc;
            // todo add statistic here
            if ((cc = input.dequeue(stream -> output.enqueue(stream), batchSize)) <= 0)
                Concurrents.waitSleep();
            else if (logger().isDebugEnabled() && cc < forceTrace)
                logger().trace(name() + " processing: [" + cc + "] odds.");
        }
    }
}
