package com.tdl.study.java.pkg4.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/** 加载使用Crypto解密的类加载器*/
public class CryptoClassLoader extends ClassLoader {
    private int key;


    public CryptoClassLoader(int key) {
        this.key = key;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classBytes;
            classBytes = loadClassBytes(name);

            Class<?> cl = defineClass(name, classBytes, 0, classBytes.length);
            if (null == cl) throw new ClassNotFoundException(name);
            return cl;
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    /**对类文件解密并加载*/
    private byte[] loadClassBytes(String name) throws IOException {
        String cname = name.replace('.', '/') + ".caesar";
        byte[] bytes = Files.readAllBytes(Paths.get(cname));
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] - key);
        }
        return bytes;
    }
}
