package com.tdl.study.crawler.output;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class StoreImpl<V> extends Thread implements Store<V> {

    protected Writer writer = null;
    private LinkedBlockingQueue<V> queue = new LinkedBlockingQueue<>();

    protected StoreImpl() {}

    @Override
    public void run() {
        while (true) try {
            write(queue.take());
        } catch (Exception e) {
            throw new RuntimeException("failed to write item.", e);
        }
    }

    public abstract void write(V item) throws IOException;

    @Override
    public void prepare() {
        this.start();
    }

    @Override
    public void write(Iterable<V> items) {
        items.forEach(item -> {
            try {
                queue.put(item);
            } catch (InterruptedException e) {
                throw new RuntimeException("failed to put item into queue.");
            }
        });
    }

    @Override
    public void close() {
        try {
            while (!queue.isEmpty()) Thread.sleep(100);
            writer.flush();
            writer.close();
        } catch (IOException | InterruptedException ignored) {}
    }
}
