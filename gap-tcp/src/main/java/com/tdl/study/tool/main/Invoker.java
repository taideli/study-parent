/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.main;

import com.tdl.study.tool.IOs;
import com.tdl.study.tool.gap.WaiterImpl;
import com.tdl.study.tool.kcp.KcpOnFile;
import com.tdl.study.tool.tcp.Client;
import com.tdl.study.tool.util.Tuple2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Invoker extends WaiterImpl {

    private final Map<Long, Tuple2<KcpOnFile, Client>> tuples;

    public Invoker(String[] args) throws IOException {
        super(EXT_REQ, EXT_RESP, args);
        tuples = new ConcurrentHashMap<>();
    }

    @Override
    public void seen(UUID key, InputStream in) {
        // TODO req 转换成KCP数据包，KCP再把收到的数据转成TCP请求，发送请求，把接收到的结果写成文件
        byte[] data = null;
        try {
            data = IOs.readBytes(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null == data || 0 == data.length) {
            System.err.println("invalid request, ignore it");
            return;
        }
        System.err.println("invoker seen size:" + data.length);

        long conv = KcpOnFile.parseConv(data);
        Tuple2<KcpOnFile, Client> tuple = tuples.get(conv);
        if (null == tuple) {
            System.out.println("a new kcp buf arrive, accept it with new kcp.");
            tuple = newTupleInstance(conv);
            tuples.put(conv, tuple);
            tuple._1().start();
            tuple._2().start();
        }
        KcpOnFile kcp = tuple._1();
        int ret = kcp.Input(data);
        if (0 != ret) {
            System.err.println("ret != 0");
        }
        // touch(dumpDest, key.toString() + touchExt, resp::save);
    }

    private Tuple2<KcpOnFile, Client> newTupleInstance(long conv) {
        KcpOnFile kcp = new KcpOnFile(conv);
        Client cli = new Client(host, port);

        kcp.setOmit(bytes -> {
            UUID key = UUID.randomUUID();
            try {
                touch(dumpDest, key.toString() + touchExt, out -> KcpOnFile.save(bytes, out));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        kcp.setUsing(data -> {
            System.err.println("kcp recv data size:" + data.length);
            cli.send(data);
        });

        cli.setChannelRegisterHandler(ch -> {});
        cli.setBufferReadHandler(data -> {});
        cli.setChannelUnregisterHandler(ch -> {});

        Tuple2<KcpOnFile, Client> tuple = new Tuple2<>(kcp, cli);
        return tuple;
    }

    @Override
    public void run() {
        watcher.joining();
    }

    public static void main(String args[]) throws InterruptedException, IOException {
        String[] arg = new String[] {"169.254.141.14", "6000", "E:/pool/responses", "E:/pool/requests" };
        Invoker inst = new Invoker(arg);
//        Invoker inst = new Invoker(args);
        inst.start();
        inst.join();
    }
}
