/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */
package xyz.tacchang;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 */
public class LocalLog {

    private static final ThreadLocal<LocalLog> LOCAL_LOGGER = new ThreadLocal<>();
    
    private final Logger logger;
        
    private LocalLog(final Logger logger) {
        this.logger = logger;
    }
    
    private static class LocalFormatter extends Formatter {

        @Override
        public synchronized String format(LogRecord record) {
            final StringBuffer message = new StringBuffer(512);
            long millis = record.getMillis();
            String time = String.format("%tF %<tT.%<tL", millis);
            message.append(time);
            message.append(' ');
            message.append(record.getThreadID());
            message.append(' ');
            message.append(" [");
            message.append(record.getLevel().toString());
            message.append("] ");
            message.append(formatMessage(record));
            message.append('\n');
            Throwable throwable = record.getThrown();
            if (throwable != null) {
                message.append(throwable.toString());
                message.append('\n');
                for (StackTraceElement trace : throwable.getStackTrace()) {
                    message.append('\t');
                    message.append(trace.toString());
                    message.append('\n');
                }
            }

            return message.toString();
        }
    }
    
    public static Logger getThreadLocalLogger() {
        if (LOCAL_LOGGER.get() == null) {
            Logger log = Logger.getLogger(LocalLog.class.getName());
            if ((System.getProperty("java.util.logging.config.file") == null)
                && (System.getProperty("java.util.logging.config.class") == null)) {
                Handler handler = new ConsoleHandler();
                Formatter formatter = new LocalFormatter();
                handler.setFormatter(formatter);
                log.addHandler(handler);
                log.setUseParentHandlers(false);
                log.setLevel(Level.ALL);
            }
            LOCAL_LOGGER.set(new LocalLog(log));
        }

        return LOCAL_LOGGER.get().getLogger();
    }
    
    private Logger getLogger() {
        return this.logger;
    }
    
    public static void finest(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.finest(String.format(format, args));
    }
    
    public static void finer(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.finer(String.format(format, args));
    }
    
    public static void fine(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.fine(String.format(format, args));
    }
    
    public static void config(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.config(String.format(format, args));
    }
    
    public static void info(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.info(String.format(format, args));
    }
    
    public static void warning(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.warning(String.format(format, args));
    }
    
    public static void severe(final String format, Object... args) {
        final Logger log = getThreadLocalLogger();
        log.severe(String.format(format, args));
    }
}
