/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.java.pkg5.join;

public class MyThread extends Thread {
    int i = 10;
    @Override
    public void run() {
        while (i-- >= 0) {
            System.out.println("My thread is running");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
