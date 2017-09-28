package com.tdl.study.zk.ch3.e2;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class Master implements Watcher {
    private ZooKeeper zk;
    private String hostPort;

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    private void startZK() throws IOException {
        zk = new ZooKeeper(hostPort, 15 * 1000, this);
    }

    private void stopZK() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    String serverID = Integer.toHexString(new Random().nextInt());
    boolean isLeader = false;

    boolean checkMaster() {
        while (true) {
            try {
                Stat stat = new Stat();
                byte[] data = zk.getData("/ch3-e2-master", false, stat);
                isLeader = new String(data).equals(serverID);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void runForMaster() {
        while (true) {
            try {
                zk.create("/ch3-e2-master", serverID.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                isLeader = false;
                e.printStackTrace();
            }
            if (checkMaster()) break;
        }
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (null == args || 0 == args.length) {
            System.out.println("Usage: " + Master.class.getName() + " <server>\n");
            System.exit(1);
        }
        Master m = new Master(args[0]);
        m.startZK();
        m.runForMaster();
//        TimeUnit.SECONDS.sleep(6);
        if (m.isLeader) {
            System.out.println("I'm the leader");
        } else {
            System.out.println("I'm NOT the leader");
        }
        m.stopZK();
    }
}
