package com.tdl.study.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSystemTest {
    private Configuration conf = new Configuration();
    private FileSystem fs = FileSystem.newInstance(conf);

    private FileSystemTest() throws IOException {}

    public void listStatus() throws IOException {

        Stream.of(fs.listStatus(new Path("/")))
                .map(FileStatus::getPath).forEach(System.out::println);
        System.err.println("=============\n");
        Stream.of(fs.globStatus(new Path("/*"), path -> true)).forEach(System.out::println);
        fs.close();
    }

    public void createFile() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename =  sdf.format(new Date());
        Path path = new Path("./create_file_test/" + filename);
        if (!fs.exists(path.getParent())) System.out.println(path.getParent() + "  doesn't exists, create it.");
        FSDataOutputStream dos = fs.create(path, true);
        InputStream is = new ByteArrayInputStream(("this file generated at " + filename).getBytes());
        IOUtils.copyBytes(is, dos, 4096, false);
//        dos.hflush();  // notes by Taideli@2018/5/11_16:58 强行把数据刷新到DataNode(保证已在DataNode的内存中，但不保证已经在硬盘上)
//        dos.hsync(); // notes by Taideli@2018/5/11_17:00 强行把数据刷新到DataNode的硬盘上
        is.close();
        dos.close();  // notes by Taideli@2018/5/11_17:01 已经隐含包含了hflush方法
//        fs.close();
    }

    public void copyFile() throws IOException {
        Path path = fs.getHomeDirectory();
        Path[] paths = FileUtil.stat2Paths(fs.listStatus(path));
//        fs.close();
    }

    /*文件搜索时的通配符*/
    public void globbing() throws IOException {
        Path path = new Path("/");
        FileStatus[] fileStatus = fs.globStatus(path);
        System.out.println("not filtered");
        for (FileStatus status : fileStatus) {
            System.out.println(status);
        }
        FileStatus[] ffs = fs.globStatus(path, (p -> p.toString().contains("GSOD")));
        System.out.println("filtered");
        for (FileStatus status : ffs) {
            System.out.println(status);
        }
    }

    public void readFile() throws IOException {
        Path path = new Path("/user/root/hadoop_fs_help.txt");
        FSDataInputStream dis = fs.open(path);
//        dis.read()
        dis.close();

    }

    public void codec() {
        String ipath = "/user/root/804570-99999-2001.op.gz";
        Path path = new Path(ipath);
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        CompressionCodec codec = factory.getCodec(path);
        if (null == codec) {
            System.err.println("No codec found for " + path.toString());
            System.exit(1);
        }
        String opath = CompressionCodecFactory.removeSuffix(ipath, codec.getDefaultExtension());
        try (InputStream is = codec.createInputStream(fs.open(path));
             OutputStream os = fs.create(new Path(opath))) {
            IOUtils.copyBytes(is, os, conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static void main(String args[]) throws InvocationTargetException, IllegalAccessException, IOException {
        FileSystemTest test = new FileSystemTest();
        List<Method> methods = methods(test);
        if (0 == args.length) {
            System.out.println("run all public non-parameter & non-value-return functions, total: " + methods.size());
            for (Method method : methods) {
                System.out.println("\n**************************************************" +
                                   "\n      invoking method: " + method.getName() +
                                   "\n**************************************************");
                method.invoke(test);
            }
        } else {
            for (String func : args) {
                Method method = methods.stream().filter(m -> m.getName().equals(func)).findFirst().orElse(null);
                if (null == method) {
                    System.out.println("no such method that meeting the condition");
                } else {
                    System.out.println("\n**************************************************" +
                                       "\n      invoking method: " + method.getName() +
                                       "\n**************************************************");
                    method.invoke(test);
                }
            }
        }

    }

    private static List<Method> methods(Object object) {
        if (null == object) return new ArrayList<>();
        return Stream.of(object.getClass().getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.getReturnType() == Void.TYPE)
                .collect(Collectors.toList());
    }
}
