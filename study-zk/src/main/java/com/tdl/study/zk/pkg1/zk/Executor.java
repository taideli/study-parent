/**
 * Created by Taideli on 2017/6/21.
 */
package com.tdl.study.zk.pkg1.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
    private String filename;
    private String[] exec;
    private ZooKeeper zk;
    private DataMonitor dm;

    public static void main(String args[]) {
        if (args.length < 4) {
            System.err.println("Usage: Executor host-port znode filename program");
            System.exit(2);
        }

        String hostPort = args[0];
        String znode = args[1];
        String filename = args[2];
        String exec[] = new String[args.length - 3];
        System.arraycopy(args, 3, exec, 0, exec.length);
        try {
            new Executor().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Executor(String hostPort, String znode, String filename, String exec[]) throws IOException {
        this.filename = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, null, this);
//                http://www.tuicool.com/articles/a6BZBru
    }

    @Override
    public void run() {
        try {
            synchronized (this) {while (!dm.dead) wait();}
        } catch (InterruptedException ignored) {}
    }
}
