package com.tdl.study.hadoop.hdfs;

import com.tdl.study.hadoop.TestUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 序列化的相关测试代码
 */
public class Serialization extends TestUtil {

    public Serialization() {
        super();
    }

    /**也可以用hadoop fs -text /user/root/numbers.seq查看文件内容*/
    public void seqFileRead() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);
        Path path = new Path("/user/root/numbers.seq");
        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(fs, path, conf);

            System.out.println("meta: " + reader.getMetadata());

            Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
            long position = reader.getPosition();
            System.out.println("position: " + position);
//            reader.seek(position + 20);
            while (reader.next(key, value)) {
                String syncSeen = reader.syncSeen() ? "*" : "";
                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key, value);
                position = reader.getPosition();
            }
        } finally {
            IOUtils.closeStream(reader);
        }
    }

    public void seqFileWrite() throws IOException {
        String[] DATA = {
            "One, two, buckle my shoe",
            "Three, four, shut the door",
            "Five, six, pick up sticks",
            "Seven, eight, lay them straight",
            "Nine, ten, a big fat hen"
        };
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.newInstance(conf);
        Path path = new Path("/user/root/numbers.seq");
        IntWritable key = new IntWritable();
        Text value = new Text();
        SequenceFile.Writer writer = null;
        try {
            writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());
            for (int i = 0; i < 100; i++) {
                key.set(100 -i);
                value.set(DATA[i % DATA.length]);
                System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public void text() {
        Text t = new Text("\u0041\u00DF\u6771\uD801\uDC00");
        ByteBuffer buf = ByteBuffer.wrap(t.getBytes(), 0, t.getLength());
        int cp;
        while (buf.hasRemaining() && -1 != (cp = Text.bytesToCodePoint(buf))) {
            System.out.println(Integer.toHexString(cp));
        }
    }

    public void serializeAnddeserialize() throws IOException {
        byte[] str = serialize(new Text("good"));
        System.out.println("serialize: " + Arrays.toString(str));
        Text dt = new Text();
        deserialize(dt, str);
        System.out.println("deserialize: " + dt.toString());

    }



    private static byte[] serialize(Writable writable) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        writable.write(dos);
        dos.close();
        return baos.toByteArray();
    }

    private static byte[] deserialize(Writable writable, byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        writable.readFields(dis);
        dis.close();
        return bytes;
    }
}
