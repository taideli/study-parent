package com.tdl.study.java.pkg2.udp;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.UUID;

public class UdpSender {
    public static void main(String args[]) {

        try {
            InetAddress address = InetAddress.getLocalHost();
            int port = 6000;
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                String msg = UUID.randomUUID().toString();
                byte[] buf = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                System.out.println(new Date().toString() + " send: " + msg);
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
