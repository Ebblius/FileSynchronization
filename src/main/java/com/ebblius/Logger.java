package com.ebblius;

import org.apache.logging.log4j.LogManager;

public class Logger {

    private final org.apache.logging.log4j.Logger log4jLogger;

    Logger() {
        log4jLogger = LogManager.getLogger();
    }

    private static final class InstanceHolder {
        private static final Logger instance = new Logger();
    }

    public static Logger getInstance() {
        return InstanceHolder.instance;
    }

    public void warn(Object message) {
        log4jLogger.warn(message);
    }

    public void info(Object message) {
        log4jLogger.info(message);
    }

    public void error(Object message) {
        log4jLogger.error(message);
    }

    public void fatal(Object message) {
        log4jLogger.fatal(message);
    }
}
