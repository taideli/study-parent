/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.main;

import com.tdl.study.tool.gap.WaiterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Dispatcher extends WaiterImpl {

    public Dispatcher(String... args) throws IOException {
        super(EXT_RESP, EXT_REQ, args);
    }

    @Override
    public void seen(UUID key, InputStream in) {

    }

    @Override
    public void run() {
        // TODO 启动KCP服务
        // TODO 启动TCP server服务
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Dispatcher inst = new Dispatcher(args);
        inst.start();
        inst.join();
    }
}
