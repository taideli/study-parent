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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HFilesInput extends InputImpl<Cell> {
    private final FileSystem fs;
    private List<HFileScanner> scanners;
    private Configuration conf;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


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
        scanners = fileStatuses.stream().map(s ->
                getScanner(s.getPath())).filter(Objects::nonNull).collect(Collectors.toList());
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

    private HFileScanner getScanner(Path path) {
        if (null != path) try {
            HFile.Reader reader = HFile.createReader(fs, path, new CacheConfig(conf), conf);
            reader.loadFileInfo();
            HFileScanner scanner = reader.getScanner(false, true);
            if (!scanner.seekTo()) {
                logger().info("{} is empty, ignore this file.", path);
                return null;
            }
            return scanner;
        } catch (IOException e) {
            logger().error("failed to create reader for {}", path);
        }
        return null;
    }

    @Override
    protected Cell dequeue() {
        if (scanners.size() <= 0) return null;
        HFileScanner scanner = scanners.get(0);
        if (lock.writeLock().tryLock()) {
            try {
                if (!scanner.next()) {
                    scanners.remove(scanner);
                    scanner = scanners.get(0);
                    logger().info("process hfile: {}", scanner.getReader().getFileContext().getHFileName());
                }
            } catch (IOException e) {
                lock.writeLock().unlock();
            }
            Cell cell = scanner.getKeyValue();
            lock.writeLock().unlock();
            return cell;
        }
        return null;
    }

    @Override
    public void close() {
        try {
            fs.close();
        } catch (IOException ignored) {}
        super.close();
    }
}
