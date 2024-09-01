package com.ebblius;

import org.apache.logging.log4j.LogManager;

public class Logger {

    private static Logger instance;
    private final org.apache.logging.log4j.Logger logger;

    private Logger() {
        logger = LogManager.getLogger();
    }

    public void warn(Object message) {
        logger.warn(message);
    }

    public void info(Object message) {
        logger.info(message);
    }

    public void error(Object message) {
        logger.error(message);
    }

    public void fatal(Object message) {
        logger.fatal(message);
    }

    public static Logger getInstance() {
        synchronized (Logger.class) {
            if (instance == null)
                instance = new Logger();
        }
        return instance;
    }
}
