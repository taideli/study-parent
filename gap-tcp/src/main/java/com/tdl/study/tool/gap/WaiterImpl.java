/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.gap;

import com.tdl.study.tool.Watcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class WaiterImpl extends Thread implements Waiter {
    public static final String EXT_REQ = ".req";
    public static final String EXT_RESP = ".rep";
    public static final String EXT_WORKING = ".working";
    public static final String EXT_FINISHED = ".finished";

    protected final String host;
    protected final int port;
    protected final String touchExt;
    protected final Path dumpDest;
    protected final Watcher watcher;

    public WaiterImpl(String watchExt, String touchExt, String... args) throws IOException {
        super();
        setName(getClass().getSimpleName() + "Thread");
        this.touchExt = touchExt;

        host = args[0];
        port = Integer.parseInt(args[1]);
        dumpDest = Paths.get(args[2]);

        Path[] watchs = new Path[] {Paths.get(args[3])};
        watcher = new Watcher(this::watch, watchs[0], watchExt, StandardWatchEventKinds.ENTRY_CREATE);
        watcher.start();
    }

    public abstract void seen(UUID key, InputStream in) throws IOException;

    @Override
    public void watch(Path from) {
        String fname = from.getFileName().toString();
        UUID key = UUID.fromString(fname.substring(0, fname.lastIndexOf(".")));
        Path working = from.getParent().resolve(fname + EXT_WORKING);

        try {
            Files.move(from, working, StandardCopyOption.ATOMIC_MOVE);
            try (InputStream is = Files.newInputStream(working, StandardOpenOption.READ)){
                seen(key, is);
            } finally {
                Files.move(working, from.getParent().resolve(fname + EXT_FINISHED), StandardCopyOption.ATOMIC_MOVE);
            }
        } catch (IOException e) {
            System.err.println("File read fail on [" + from.toAbsolutePath().toString() + "]");
        }
    }

    @Override
    public void touch(Path dest, String filename, Consumer<OutputStream> outputting) throws IOException {
        Path working = dest.resolve(filename + EXT_WORKING), worked = dest.resolve(filename);
        try (OutputStream os = Files.newOutputStream(working, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            outputting.accept(os);
        } finally {
            Files.move(working, worked, StandardCopyOption.ATOMIC_MOVE);
            long s = Files.size(worked);
//            System.err.println("Data saved: [" + filename + "], size: [" + s + "].");
        }
    }
}
