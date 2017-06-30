package com.tdl.study.java.pkg2.udp;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class UdpReceiver {
    public static void main(String args[]) {
        try {
//            InetAddress address = InetAddress.getByName("172.16.16.116");
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 39991;
            DatagramSocket socket = new DatagramSocket(port, address);

            while (true) {
                byte[] buf = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String context = new String(packet.getData());
                System.out.println(new Date().toString() + " receive :" + context);
                String back = new StringBuffer(context).reverse().toString();
//                packet.setData(back.getBytes());
                socket.send(packet);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
