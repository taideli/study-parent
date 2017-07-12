package com.tdl.study.crawler.output;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConsoleStore extends StoreImpl<String> {
    @Override
    public void prepare() {
        writer = new OutputStreamWriter(System.out);
        super.prepare();
    }

    @Override
    public void write(String item) throws IOException {
        writer.write(item + "\n");
    }
}
