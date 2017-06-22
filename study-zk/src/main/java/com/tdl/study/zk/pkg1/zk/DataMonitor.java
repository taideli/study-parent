/**
 * Created by Taideli on 2017/6/21.
 */
package com.tdl.study.zk.pkg1.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

/**
 *  monitors the data in the ZooKeeper tree
 */
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {
    private ZooKeeper zk;
    private String znode;
    private Watcher chainedWatcher;
    private DataMonitorListener listener;
    public boolean dead;
    private byte[] prevData;

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode, true, this, null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // retry errors
                zk.exists(znode, true, this, null);
                return;
        }

        byte[] b = null;
        if (exists) {
            try {
                b = zk.getData(znode, false, null);
            } catch (KeeperException e) {
                // we don't need to worry about recovering now, the watch
                // callbacks will kick off any exception handling
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }
        if ((null == b && b != prevData) || (null != b && !Arrays.equals(prevData, b))) {
            listener.exists(b);
            prevData = b;
        }
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Watcher.Event.EventType.None) {
            // we are being told that the state of the connection has changed
            switch (event.getState()) {
                case SyncConnected:
                    // In this particular example we don't need to do anything
                    // here - watches are automatically re-registered with
                    // server and any watches triggered while the client was
                    // disconnected will be delivered (in order of course)
                    break;
                case Expired:
                    // It's all over
                    dead = true;
                    listener.closing(KeeperException.Code.SessionExpired);
                    break;
            }
        } else {
            if (null != path && path.equals(znode)) {
                // something has changed on the node, let's find out
                zk.exists(znode, true, this, null);
            }
        }
        if (null != chainedWatcher) {
            chainedWatcher.process(event);
        }
    }

    public interface DataMonitorListener {
        /**
         * The existence status of the node has changed.
         * @param data
         */
        void exists(byte[] data);

        /**
         * The Zookeeer session is no longer valid.
         * @param rc zk reason code
         */
        void closing(int rc);
    }
}
