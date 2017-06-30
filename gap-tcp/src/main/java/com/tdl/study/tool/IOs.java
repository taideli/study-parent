/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOs {

    public static Path mkdirs(Path path) {
        if (Files.exists(path) && Files.isDirectory(path)) return path;
        try {
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-existed directory [" + path + "] could not be created.");
        }
    }

    public static int readInt(InputStream in) throws IOException {
        int b, i = 0, offset = -8;
        if ((b = in.read()) >= 0) i |= b << (offset += 8);
        if ((b = in.read()) >= 0) i |= b << (offset += 8);
        if ((b = in.read()) >= 0) i |= b << (offset += 8);
        if ((b = in.read()) >= 0) i |= b << (offset += 8);
//        logger.trace("Read int: " + i);
        return i;
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        int l = readInt(in);
//        logger.trace(() -> "readBytes: length[" + l + "]");
        if (l < 0) return null;
        if (l == 0) return new byte[0];
        byte[] bytes = new byte[l];
        for (int i = 0; i < l; i++) {
            int b;
            if ((b = in.read()) < 0)//
                throw new EOFException("not full read but no data remained, need [" + l + ", now [" + i + "]]");
            else bytes[i] = (byte) b;
        }
//        logger.trace(() -> "readBytes: data[" + l + "]");
        return bytes;
    }

    public static byte[] readAll(final InputStream in) {
        if (null == in) throw new IllegalArgumentException("null byte array is not allowed.");
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[1024];
            int n;
            while (-1 != (n = in.read(buffer)))
                os.write(buffer, 0, n);
            byte[] b = os.toByteArray();
//            logger.trace(() -> "Read all: " + b.length);
            return b;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <S extends OutputStream> S writeInt(S out, int i) throws IOException {
//        logger.trace("Write int: " + i);
        out.write(i & 0x000000FF);
        i >>= 8;
        out.write(i & 0x000000FF);
        i >>= 8;
        out.write(i & 0x000000FF);
        i >>= 8;
        out.write(i & 0x000000FF);
        return out;
    }

    public static <S extends OutputStream> S writeBytes(S out, byte[] b) throws IOException {
        if (null == b) {
            writeInt(out, -1);
//            logger.trace(() -> "writeBytes: length[-1]");
        } else {
            writeInt(out, b.length);
//            logger.trace(() -> "writeBytes: length[" + b.length + "]");
            if (b.length > 0) {
                out.write(b);
//                logger.trace(() -> "writeBytes: data[" + b.length + "]");
            }
        }
        return out;
    }
}
