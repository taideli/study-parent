package com.tdl.study.core.log;

import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger implements Serializable {
    private static final boolean async;
    private static final AtomicInteger tn;
    private static final ThreadGroup tg;
    private static final ExecutorService ex;
    private final org.slf4j.Logger logger;

    static {
        async = Boolean.parseBoolean(System.getProperty("com.tdl.study.core.log.async.enable", "true"));
        if (async) {
            tn = new AtomicInteger();
            tg = new ThreadGroup("TdlLogThread");
            ex = Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(tg, r, tg.getName() + "#" + tn.getAndIncrement());
                t.setDaemon(true);
                return t;
            });
        } else {
            tn = null;
            tg = null;
            ex = null;
        }
    }

    private Logger(org.slf4j.Logger logger) {
        super();
        this.logger = logger;
    }

    public static final Logger getLogger(CharSequence name) {
        return new Logger(LoggerFactory.getLogger(name.toString()));
    }

    public static final Logger getLogger(Class<?> clazz) {
        return new Logger(LoggerFactory.getLogger(clazz));
    }

    public CharSequence getName() {
        return logger.getName();
    }

    private void submit(Runnable run) {
        if (null != run) {
            if (async) try {
                ex.submit(run);
            } catch (RejectedExecutionException e) {
                run.run();
            }
            else run.run();
        }
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean trace(CharSequence msg) {
        submit(() -> logger.trace(msg.toString()));
        return true;
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean debug(CharSequence msg) {
        submit(() -> logger.debug(msg.toString()));
        return true;
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean info(CharSequence msg) {
        submit(() -> logger.info(msg.toString()));
        return true;
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean warn(CharSequence msg) {
        submit(() -> logger.warn(msg.toString()));
        return true;
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean error(CharSequence msg) {
        submit(() -> logger.error(msg.toString()));
        return true;
    }

}
