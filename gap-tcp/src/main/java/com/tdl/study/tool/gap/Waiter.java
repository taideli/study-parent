/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool.gap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface Waiter extends Runnable {
    void watch(Path from);

    void touch(Path dest, String filename, Consumer<OutputStream> outputting) throws IOException;

}
