/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Watcher extends Thread {
    private static final Map<FileSystem, WatchService> watchers = new ConcurrentHashMap<>();
    private final Path dest;
    private final WatchService watcher;
    private final Set<WatchEvent.Kind<?>> events;
    private Consumer<Path> handler;
    private final String ext;

    public Watcher(Consumer<Path> using, String ext) throws IOException {
        this(using, ext, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public Watcher(Consumer<Path> using, String ext, WatchEvent.Kind<?> watching, WatchEvent.Kind<?>... watchings) throws IOException {
        this (using, Paths.get(""), ext, watching, watchings);
    }

    public Watcher(Consumer<Path> using, Path path, String ext, WatchEvent.Kind<?> watching, WatchEvent.Kind<?>... watchings) throws IOException {
        super();
        dest = IOs.mkdirs(path);
        System.err.println("Directory [" + path + "] is been watched.");
        setName("FileWatcher:" + dest.toAbsolutePath());
        setDaemon(true);
        this.ext = ext;
        this.handler = using;
        events = new HashSet<>();
        events.add(watching);
        events.addAll(Arrays.asList(watchings));
        watcher = watchers.computeIfAbsent(dest.getFileSystem(), fs -> {
            try {
                return fs.newWatchService();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (null == watcher) throw new IOException("Watcher could not be initialized.");
    }

    @SuppressWarnings("unckecked")
    @Override
    public void run() {
        WatchKey key;
        try {
            key = dest.register(watcher, events.toArray(new WatchEvent.Kind<?>[events.size()]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                break;
            }
            key.pollEvents().parallelStream()
                .filter(ev -> events.contains(ev.kind()))
                .map(ev -> ((WatchEvent<Path>) ev).context())
                .filter(p -> p.toString().endsWith(ext))
                .map(dest::resolve).forEach(handler);
            if (!key.reset()) break;
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        new Watcher(f -> System.out.println(f.toAbsolutePath()), ".txt", ENTRY_CREATE).join();
    }

    public void joining() {
        try {
            join();
        } catch (InterruptedException ignored) {}
    }
}
