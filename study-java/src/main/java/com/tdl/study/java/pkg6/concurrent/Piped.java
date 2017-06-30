package com.tdl.study.java.pkg6.concurrent;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 它主要用于线程之间的数据传输，传输的媒介为内存
 *
 * 包括了如下4种具体实现：PipedOutputStream、PipedInputStream、
 * PipedReader和PipedWriter，前两种面向字节，而后两种面向字符
 *
 * 对于Piped类型的流，必须先要进行绑定，也就是调用connect()方法，如果没有将输入/输
 * 出流绑定起来，对于该流的访问将会抛出异常
 */
public class Piped {
    public static void main(String[] args) throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();

        // notes by taidl@2017/6/30_16:42 要先将输出和输入连接，否则在使用是会抛出IOException
        out.connect(in);

        Thread printThread = new Thread(new Print(in), "PrintThread");

        printThread.start();

        int receive = 0;
        try {
            while (-1 != (receive = System.in.read())) {
                out.write(receive);
            }
        } finally {
            out.close();
        }
    }

    static class Print implements Runnable {
        private PipedReader in;

        public Print(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while (-1 != (receive = in.read())) {
                    System.out.print((char) receive);
                }
            } catch (IOException ignored) {}
        }
    }
}
