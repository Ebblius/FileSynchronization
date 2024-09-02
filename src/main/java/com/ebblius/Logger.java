package com.ebblius;

import org.apache.logging.log4j.LogManager;

public class Logger {

    private static Logger instance;
    private final org.apache.logging.log4j.Logger log4jLogger;

    Logger() {
        log4jLogger = LogManager.getLogger();
    }

    public static Logger getInstance() {
        synchronized (Logger.class) {
            if (instance == null)
                instance = new Logger();
        }
        return instance;
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
