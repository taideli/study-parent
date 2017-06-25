/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.tcp;

import java.util.function.Consumer;

public class Client {
    private final String host;
    private final int port;
    private Consumer<String> channelRegisterHandler;
    private Consumer<byte[]> bufferReadHandler;
    private Consumer<String> channelUnregisterHandler;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void setChannelRegisterHandler(Consumer<String> channelRegisterHandler) {
        this.channelRegisterHandler = channelRegisterHandler;
    }

    public void setBufferReadHandler(Consumer<byte[]> bufferReadHandler) {
        this.bufferReadHandler = bufferReadHandler;
    }

    public void setChannelUnregisterHandler(Consumer<String> channelUnregisterHandler) {
        this.channelUnregisterHandler = channelUnregisterHandler;
    }

    public void send(byte[] data) {
        System.err.println("need to be complete send method");
    }

    public void start() {
        System.err.println("need to be complete start method");
    }
}
