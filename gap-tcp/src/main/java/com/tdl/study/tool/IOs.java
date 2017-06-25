/**
 * Created by Taideli on 2017/6/25.
 */
package com.tdl.study.tool;

import java.io.IOException;
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
}
