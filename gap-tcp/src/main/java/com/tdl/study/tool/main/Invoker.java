/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.main;

import com.tdl.study.tool.gap.WaiterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Invoker extends WaiterImpl {

    public Invoker(String[] args) throws IOException {
        super(EXT_REQ, EXT_RESP, args);
    }

    @Override
    public void seen(UUID key, InputStream in) {
        // TODO req 转换成KCP数据包，KCP再把收到的数据转成TCP请求，发送请求，把接收到的结果写成文件
        // touch(dumpDest, key.toString() + touchExt, resp::save);
    }

    @Override
    public void run() {
        watcher.joining();
    }

    public static void main(String args[]) throws InterruptedException, IOException {
        Invoker inst = new Invoker(args);
        inst.start();
        inst.join();
    }
}
