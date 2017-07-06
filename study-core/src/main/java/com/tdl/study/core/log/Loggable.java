package com.tdl.study.core.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Loggable {
    Map<Class<? extends Loggable>, Logger> LOGGERS = new ConcurrentHashMap<>();

    default Logger logger() {
        return LOGGERS.computeIfAbsent(this.getClass(), Logger::getLogger);
    }
}
