package com.tdl.study.hadoop.hdfs;

import com.tdl.study.hadoop.TestUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Serialization extends TestUtil {

    public Serialization() {
        super();
    }

    public void t1() throws IOException {
        byte[] str = serialize(new Text("good"));
        System.out.println(Arrays.toString(str));
    }



    public static byte[] serialize(Writable writable) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        writable.write(dos);
        dos.close();
        return baos.toByteArray();
    }
}
