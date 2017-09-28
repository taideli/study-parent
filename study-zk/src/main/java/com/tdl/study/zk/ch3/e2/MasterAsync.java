package com.tdl.study.zk.ch3.e2;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class MasterAsync implements Watcher {
    private ZooKeeper zk;
    private String hostPort;

    public MasterAsync(String hostPort) {
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

    private void checkMaster() {
        zk.getData("/ch3-e2-master", false, (rc, path, ctx, data, stat) -> {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    break;
                case NONODE:
                    System.out.println("node do not exist." + KeeperException.create(KeeperException.Code.get(rc), path));
                    break;
                case OK:
                    break;
                default:
                    System.out.println("sth. went wrong when running master " + path + ", " + KeeperException.Code.get(rc));
            }
        }, null);
    }

    private void runForMaster() {
        zk.create("/ch3-e2-master", serverID.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
            (rc, path, ctx, name) -> {
                switch (KeeperException.Code.get(rc)) {
                    case CONNECTIONLOSS:
                        // 一般为网络异常，需要重试
                        runForMaster();
                        break;
                    case NODEEXISTS:
                        break;
                    case OK:
                        break;
                    default:
                        System.out.println("something went wrong when running for master, " + KeeperException.create(KeeperException.Code.get(rc), path));
                }
            }, null);
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        if (null == args || 0 == args.length) {
            System.out.println("Usage: " + MasterAsync.class.getName() + " <server>\n");
            System.exit(1);
        }
        MasterAsync m = new MasterAsync(args[0]);
        m.startZK();
        m.runForMaster();
        TimeUnit.SECONDS.sleep(6);
        m.stopZK();
    }
}
