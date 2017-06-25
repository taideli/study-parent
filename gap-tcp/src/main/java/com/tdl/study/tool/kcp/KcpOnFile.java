/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.kcp;

public class KcpOnFile extends KCP {

    public KcpOnFile(long conv_) {
        super(conv_);
    }

    @Override
    protected void output(byte[] buffer, int size) {

    }


    public void start() {
        // TODO 先启动底层写文件线程 再启动接收线程
    }
}
