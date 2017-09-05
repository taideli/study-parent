package com.tdl.study.java.test;

public class DeamonThread {

    public static void main(String[] args) {
        Thread d = new Thread(() -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("shutdown...")));
            while (true) {
                try {
                    System.out.println("deamon thread running...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "daemonThread");
        d.setDaemon(true);
        d.start();
    }
}
