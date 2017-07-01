/**
 * Created by Taideli on 2017/7/1.
 */
package com.tdl.study.tool.main.udp;

import com.tdl.study.tool.IOs;
import com.tdl.study.tool.gap.WaiterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.UUID;

public class Invoker extends WaiterImpl {
    private final int gapPort;
    private final Selector selector;

    public Invoker(int port, String watchExt, String touchExt, String... args) throws IOException {
        super(watchExt, touchExt, args);
        this.gapPort = port;
        selector = Selector.open();
    }

    @Override
    public void seen(UUID key, InputStream in) throws IOException {
        int port = IOs.readInt(in);
        ByteBuffer wbuf = ByteBuffer.wrap(IOs.readAll(in));

    }
}
