package com.tdl.study.core.io;

import com.tdl.study.core.base.Named;
import com.tdl.study.core.log.Loggable;

public interface Openable extends AutoCloseable, Loggable, Named {

    @Override
    default void close() {

    }
}
