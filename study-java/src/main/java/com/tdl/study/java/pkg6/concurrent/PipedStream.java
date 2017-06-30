package com.tdl.study.java.pkg6.concurrent;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class PipedStream {

    public static void main(String[] args) throws IOException, InterruptedException {
        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream();

        out.connect(in);

        Thread outThread = new Thread(new OuterThread(in), "OuterThread");
        outThread.start();

        out.write("hello".getBytes());
        TimeUnit.SECONDS.sleep(3);
        out.write("before end".getBytes());
        out.close();

    }

    static class OuterThread implements Runnable {
        private PipedInputStream in;

        public OuterThread(PipedInputStream in) {
            this.in = in;
        }


        @Override
        public void run() {
            byte[] data = new byte[2048];
            int received = 0;
            try {
                while (-1 != (received = in.read(data))) {
                    ByteBuffer bf = ByteBuffer.wrap(data);
                    System.out.println("read bytes size: " + received);
                    bf.clear();
                }
                System.err.println("pipe closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
