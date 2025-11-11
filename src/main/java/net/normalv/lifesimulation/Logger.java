package net.normalv.lifesimulation;

import net.normalv.lifesimulation.managers.LogManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final static String prefix = "Bobby Log";
    private final static String warnLevelInternal = "\u001b[31mWARN\u001b[0m";
    private final static String warnLevel = "WARN";
    private final static String warnANSI = "\u001b[1m\u001b[31m";
    private final static String debugLevelInteral = "\u001b[33mDEBUG\u001b[0m";
    private final static String debugLevel = "DEBUG";
    private final static String debugANSI = "\u001b[33m";
    private final static String infoLevel = "INFO";
    private final static String infoANSI = "\u001b[0m";
    private static boolean logIntern = true;
    private final static LogManager logManager = new LogManager();

    public static void warn(String warnMessage){
        if(logIntern) logInternal(messageBuilderInternal(warnLevelInternal, warnANSI+warnMessage));
        logManager.log(messageBuilder(warnLevel, warnMessage));
    }

    public static void debug(String debugMessage) {
        if(logIntern) logInternal(messageBuilderInternal(debugLevelInteral, debugANSI+debugMessage));
        logManager.log(messageBuilder(debugLevel, debugMessage));
    }

    public static void info(String infoMessage) {
        if(logIntern) logInternal(messageBuilderInternal(infoLevel, infoANSI+infoMessage));
        logManager.log(messageBuilder(infoLevel, infoMessage));
    }

    private static void logInternal(String message) {
        System.out.println(message);
    }

    private static String messageBuilderInternal(String logLevel, String content) {
        return "\u001b[1m\u001b[32m["+prefix+"]\u001b[0m:["+getCurrentTime()+"]:["+logLevel+"]: "+content;
    }
    private static String messageBuilder(String logLevel, String content) {
        return "["+prefix+"]:["+getCurrentTime()+"]:["+logLevel+"]: "+content;
    }

    private static String getCurrentTime() {return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));}

    public static void close() {
        debug("==========Closing Simulation==========");
        logManager.close();
    }
}
