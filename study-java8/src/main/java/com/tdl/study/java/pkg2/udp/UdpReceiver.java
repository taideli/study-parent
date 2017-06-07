package com.tdl.study.java.pkg2.udp;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class UdpReceiver {
    public static void main(String args[]) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            int port = 6000;
            DatagramSocket socket = new DatagramSocket(port, address);

            while (true) {
                byte[] buf = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println(new Date().toString() + " receive :" + new String(packet.getData()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
