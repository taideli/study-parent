package com.tdl.study.java.pkg6.concurrent.thread.pool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {
    static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(1);

    static String basePath;
    static int port = 8899;
    static ServerSocket server;

    public static void setPort(int port) {
        if (port > 0) SimpleHttpServer.port = port;
        else throw new IllegalArgumentException("invalid port: " + port);
    }

    public static void setBasePath(String path) {
        if (null != path && new File(path).exists() && new File(path).isDirectory()) SimpleHttpServer.basePath = path;
        else throw new IllegalArgumentException("invalid path");
    }

    public static void start() throws IOException {
        server = new ServerSocket(port);
        Socket socket = null;
        while (null != (socket = server.accept())) {
            threadPool.execute(new HttpRequestHandler(socket));
        }
        server.close();
    }

    static class HttpRequestHandler implements Runnable {
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String line = null;
            BufferedReader br = null, reader = null;
            PrintWriter out = null;
            InputStream in = null;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                String filePath = basePath + header.split(" ")[1];
                out = new PrintWriter(socket.getOutputStream());
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while (-1 != (i = in.read())) baos.write(i);

                    byte[] array = baos.toByteArray();
                    out.println("HTTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + array.length);
                    out.println("");
                    socket.getOutputStream().write(array, 0, array.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println("");
                    while (null != (line = br.readLine())) out.println(line);
                }
                out.flush();
            } catch (IOException e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            } finally {
                close(br, in, reader, out, socket);
            }
        }
    }

    private static void close(Closeable... closeables) {
        if (null != closeables) for (Closeable c : closeables) try {
            c.close();
        } catch (Exception ignored) {}
    }
}
