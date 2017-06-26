/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.main;

import com.tdl.study.tool.gap.WaiterImpl;
import com.tdl.study.tool.kcp.KcpOnFile;
import com.tdl.study.tool.util.Tuple2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher extends WaiterImpl {
    private final Map<Long, Tuple2<String, KcpOnFile>> tuples;
    private long _conv;

    public Dispatcher(String... args) throws IOException {
        super(EXT_RESP, EXT_REQ, args);
        tuples = new ConcurrentHashMap<>();
    }

    private long getConv() {
        return ++_conv;
    }

    public void channelRegisterHandler(String ch) {
        long conv = getConv();
        KcpOnFile kcp = new KcpOnFile(conv);
        tuples.put(conv, new Tuple2<>(ch, kcp));
        kcp.setOmit(bytes -> {
            UUID key = UUID.randomUUID();
            try {
                touch(dumpDest, key.toString() + touchExt, out -> KcpOnFile.save(bytes, out));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        kcp.setUsing(bytes -> {
            System.err.println("dispatcher receive high level buf, size:" + bytes.length);
        });
//        kcp.connect();
        System.err.println("git test");
        kcp.start();
        kcp.Send("hello".getBytes());

    }

    @Override
    public void seen(UUID key, InputStream in) {

    }

    @Override
    public void run() {
        // TODO 启动KCP服务
        // TODO 启动TCP server服务
        channelRegisterHandler("tcp channel");
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        String[] arg = new String[] {"169.254.141.14", "6000", "E:/pool/requests", "E:/pool/responses"};
        Dispatcher inst = new Dispatcher(arg);
//        Dispatcher inst = new Dispatcher(args);
        inst.start();
        inst.join();
    }
}
