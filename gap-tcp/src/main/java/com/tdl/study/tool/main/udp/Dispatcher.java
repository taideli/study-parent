package com.tdl.study.tool.main.udp;

import com.tdl.study.tool.IOs;
import com.tdl.study.tool.gap.WaiterImpl;
import com.tdl.study.tool.util.Threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Dispatcher extends WaiterImpl {
    private final int gapPort;
    private final DatagramChannel server;
    private final Selector selector;
    private final Map<String, Consumer<InputStream>> handles;
    public static final int UDP_DATAGRAM_MAX_LEN = 0xFFFF - 8 - 20;
    public static final String LOOP_BACK = "127.0.0.1";

    public Dispatcher(int port, String watchExt, String touchExt, String... args) throws IOException {
        super(watchExt, touchExt, args);
        this.gapPort = port;
        handles = new ConcurrentHashMap<>();

        server = DatagramChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(LOOP_BACK, gapPort));
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void seen(UUID key, InputStream in) throws IOException {
        int remotePort = IOs.readInt(in);
        int size = client(remotePort).write(ByteBuffer.wrap(IOs.readAll(in)));
    }


    @Override
    public void run() {
        while (true) {
            try {
                int n = selector.select();
                if (n > 0) {
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey k = it.next();
                        it.remove();
                        if (k.isReadable()) {
                            DatagramChannel ch = (DatagramChannel) k.channel();
                            ByteBuffer rbuf = ByteBuffer.allocate(UDP_DATAGRAM_MAX_LEN);
                            InetSocketAddress address = (InetSocketAddress) ch.receive(rbuf);
                            int remotePort = address.getPort();

                            AtomicBoolean finished = new AtomicBoolean(false);
                            UUID key = UUID.randomUUID();
                            touch(key.toString() + touchExt, out -> write(out, remotePort, rbuf, finished));
                            while (!finished.get()) {
                                Threads.sleep(10);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(OutputStream out, int port, ByteBuffer data, AtomicBoolean flag) {
        try {
            IOs.writeInt(out, port);
            IOs.writeBytes(out, data.array());
        } catch (IOException ignored) {} finally {
            flag.set(true);
        }
    }

    private DatagramChannel client(int port) throws IOException {
        DatagramChannel client = DatagramChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress(LOOP_BACK, port));
        // FIXME: 2017/6/30 不需要再注册吧，因为根本就不会监听这些
        Selector selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
        return client;
    }
}
