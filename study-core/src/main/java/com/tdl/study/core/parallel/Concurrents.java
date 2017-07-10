package com.tdl.study.core.parallel;

import com.tdl.study.core.log.Logger;

import java.util.function.Supplier;

public class Concurrents {

    private static long DEFAULT_WAIT_MS = 100;

    /**
     * wait @code DEFAULT_WAIT_MS util waiting is true
     * @param waiting condition
     * @return
     */
    public static boolean waitSleep(Supplier<Boolean> waiting) {
        while (waiting.get())
            if (!Concurrents.waitSleep()) return false;
        return true;
    }

    public static boolean waitSleep() {
        return waitSleep(DEFAULT_WAIT_MS);
    }

    public static boolean waitSleep(long millis) {
        return waitSleep(millis, null, null);
    }

    public static boolean waitSleep(long millis, Logger logger, CharSequence cause) {
        if (millis < 0) return true;
        if (null != logger && logger.isTraceEnabled())
            logger.trace("Thread [" + Thread.currentThread().getName() + "] sleep " +
                millis + "millis, cause " + cause);
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
