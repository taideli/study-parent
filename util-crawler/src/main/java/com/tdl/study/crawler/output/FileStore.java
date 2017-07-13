package com.tdl.study.crawler.output;

import java.io.FileWriter;
import java.io.IOException;

public class FileStore extends StoreImpl<String> {
    String path;

    public FileStore(String path) {
        super();
        this.path = path;
    }

    @Override
    public void prepare() {
        try {
            writer = new FileWriter(path, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.prepare();
    }

    @Override
    public void write(String item) throws IOException {
        writer.write(item + "\n");
    }
}
