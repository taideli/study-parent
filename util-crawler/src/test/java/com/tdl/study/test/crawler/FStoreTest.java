package com.tdl.study.test.crawler;

import com.tdl.study.crawler.output.FileStore;
import com.tdl.study.crawler.output.Store;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FStoreTest {

    @Test
    public void t() {
        Store<String> s = new FileStore("E:\\writeTest\\a.txt");
        List<String> strs = new ArrayList<>();
        strs.add(UUID.randomUUID().toString());
        strs.add(UUID.randomUUID().toString());
        strs.add(UUID.randomUUID().toString());

        s.prepare();
        s.write(strs);
        s.close();
    }

    @Test
    public void t2() {
        for (int i = 8; i-- > 0;) {
            t();
        }
    }
}
