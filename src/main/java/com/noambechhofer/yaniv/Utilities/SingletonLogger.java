package com.noambechhofer.yaniv.utilities;

import java.util.logging.Logger;

import com.noambechhofer.yaniv.YanivProperties;

public final class SingletonLogger {
    private static Logger logger;

    private SingletonLogger() {
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("Yaniv");
            logger.setLevel(YanivProperties.LOG_LEVEL);
        }

        return logger;
    }
}
