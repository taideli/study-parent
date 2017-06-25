/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.java.pkg5.join;

import java.util.Date;

/**
 *
 */
public class Test {

    public static void main(String args[]) throws InterruptedException {
        System.out.println(new Date());
        Thread t = new MyThread();
        t.start();
        /**join: 邀请t线程先执行，本线程暂停执行，t线程执行完成后本线程再继续执行*/
//        t.join();
        System.out.println("Test OK;");
    }
}
