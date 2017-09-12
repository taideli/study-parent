package com.tdl.study.hbase.io.pump.input;

import com.tdl.study.core.io.input.InputImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.io.hfile.CacheConfig;
import org.apache.hadoop.hbase.io.hfile.HFile;
import org.apache.hadoop.hbase.io.hfile.HFileScanner;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

public class HFilesInput extends InputImpl<Cell> {
    private final FileSystem fs;
    private ConcurrentLinkedQueue<Pair<HFileScanner, ReentrantReadWriteLock>> scanners = new ConcurrentLinkedQueue<>();
    private Configuration conf;


    public HFilesInput(boolean isOnHdfs, String path) throws IOException {
        this(HFilesInput.class.getSimpleName(), isOnHdfs, path);
    }

    public HFilesInput(String name, boolean isOnHdfs, String path) throws IOException {
        super(name);
        conf = new Configuration();
        if (isOnHdfs) {
            fs = FileSystem.get(conf);
        } else {
            fs = FileSystem.getLocal(conf);
        }

        Path p = new Path(path);
        List<FileStatus> fileStatuses = findAllMatchFilesRecursively(fs, p, (s) -> {
            try {
                return HFile.isHFileFormat(fs, s);
            } catch (IOException e) {
                return false;
            }
        });

        logger().info("find {} matched file{} in directory: {}",
                fileStatuses.size(), fileStatuses.size() <=1 ? "" : "s", fs.getFileStatus(p).getPath().toString());
        fileStatuses.forEach(s -> logger().info(s.getPath().toString()));
        fileStatuses.stream().map(FileStatus::getPath).map(this::mapScanner).filter(Objects::nonNull).forEach(s -> {
            ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
            scanners.add(new Pair<>(s, lock));
        });
        open();
    }

    private List<FileStatus> findAllMatchFilesRecursively(FileSystem fs, Path path, Predicate<FileStatus> filter) {
        List<FileStatus> statusList = new ArrayList<>();
        List<FileStatus> fileStatusesInCurrentDir = new ArrayList<>();
        try {
            fileStatusesInCurrentDir.addAll(Arrays.asList(fs.listStatus(path)));
        } catch (IOException ignored) {}
        fileStatusesInCurrentDir.stream().filter(filter).forEach(statusList::add);
        fileStatusesInCurrentDir.stream().filter(FileStatus::isDirectory)
                .forEach(sd -> statusList.addAll(findAllMatchFilesRecursively(fs, sd.getPath(), filter)));
        return statusList;
    }

    private HFileScanner mapScanner(Path path) {
        if (null != path) try {
            HFile.Reader reader = HFile.createReader(fs, path, new CacheConfig(conf), conf);
            reader.loadFileInfo();
            HFileScanner scanner = reader.getScanner(false, false);
            if (!scanner.seekTo()) {
                logger().debug("{} is empty, ignore this file.", path);
                return null;
            }
            return scanner;
        } catch (IOException e) {
            logger().error("failed to create reader for {}", path);
        }
        return null;
    }

    private static boolean scannerHasNext(HFileScanner scanner) {
        boolean nonEmpty = false;
        try {
            nonEmpty = scanner.next();
            if (!nonEmpty) scanner.getReader().close();
        } catch (IOException ignored) {}
        return nonEmpty;
    }

    @Override
    protected Cell dequeue() {
        Cell cell = null;
        Pair<HFileScanner, ReentrantReadWriteLock> pair = scanners.peek();
        if (null == pair) return null;
        HFileScanner scanner = pair.getFirst();
        ReentrantReadWriteLock lock = pair.getSecond();
        if (lock.writeLock().tryLock()) {
            cell = scanner.getKeyValue();
            if (!scannerHasNext(scanner)) {
                scanners.remove(pair);
//                scanners.poll();
            }
            lock.writeLock().unlock();
        }
        return cell;
    }

    /**
     * mark the input is empty
     * the pump will finish if return empty, otherwise waiting for new item coming
     * @return if the input is empty
     */
    @Override
    public boolean empty() {
        // jstack pid TIMED_WAITING
        return scanners.isEmpty();
    }

    @Override
    public void close() {
        try {
            fs.close();
        } catch (IOException ignored) {}
        super.close();
    }
}
