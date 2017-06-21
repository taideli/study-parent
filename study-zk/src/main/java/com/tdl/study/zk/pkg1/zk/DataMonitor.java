/**
 * Created by Taideli on 2017/6/21.
 */
package com.tdl.study.zk.pkg1.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DataMonitor {
    private ZooKeeper zk;
    private String znode;
    private Watcher chainedWatcher;
    private DataMonitorListener listener;

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);
    }

    public boolean isDead() {
        // TODO 未完成
        return false;
    }

    public interface DataMonitorListener {
        /**
         * The existence status of the node has changed.
         * @param data
         */
        void exists(byte data[]);

        /**
         * The Zookeeer session is no longer valid.
         * @param rc zk reason code
         */
        void closing(int rc);
    }
}
