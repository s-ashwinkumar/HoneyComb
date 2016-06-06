package loggi.faultinjection;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * This is a logger class file, package the log4j.
 * This class has four method which are start, log, finish, error
 *
 * The format will be as below:
 * 2016-06-01 19:18:16:000 timeZone INFO
 * [FaultInstanceId = 123456789112300001]
 * [FaultId = 00001]    [faultName = “LaunchConfiguration”]    Message
 *
 * You can use as below:
 * Loggi loggi = new Loggi("123456789112300001", className);
 * loggi.start();
 * loggi.log("hehe");
 * loggi.finish();
 * loggi.error("wrong");
 * loggi.error(Exception e);
 *
 */
public class Loggi {
    /**
     * faultInstanceId is the fault injection thread.
     */
    private String faultInstanceId;
    /**
     * faultId indicate a specific fault.
     */
    private String faultId;
    /**
     * className is the faultName.
     */
    private String className;
    /**
     * logger is the instance of log4j.
     */
    private Logger logger;

    /**
     * position for the faultId (FaultInstanceId is timeStamp + faultId).
     */
    static final int FAULTID_POSITION = 13;

    /**
     * This is the constructor, it will check whether the log file is existed,
     * and it will make a new one if not existed.
     * @param s is the faultInstanceId
     * @param c is the className (faultName)
     */
    public Loggi(final String s, final String c) throws IOException {
        File file = new File("src/main/resources/log");
        if (!file.exists())
            file.createNewFile();
        faultInstanceId = s;
        faultId = s.substring(FAULTID_POSITION);
        className = c;
        logger = Logger.getLogger("honeycomb");
    }

    /**
     * this is the start log function,
     * you should call this at the start of a fault injection.
     */
    public final void start() {
        logger.info("[FaultInstanceId = " + faultInstanceId + "]\t[FaultId = "
                + faultId + "]\t"
                + "[FaultName = " + className + "]\t"
                + "fault injection start!");
    }

    /**
     * This function can log any normal message you want to store.
     * @param s is the message you want to store.
     */
    public final void log(final String s) {
        logger.info("[FaultInstanceId = " + faultInstanceId + "]\t[FaultId = "
                + faultId + "]\t"
                + "[FaultName = " + className + "]\t"
                + s);
    }

    /**
     * this is the start log function,
     * you should call this at the end of a fault injection.
     */
    public final void finish() {
        logger.info("[FaultInstanceId = " + faultInstanceId + "]\t[FaultId = "
                + faultId + "]\t"
                + "[FaultName = " + className + "]\t"
                + "fault injection finish!");
    }

    /**
     * This function can log any error string message you want to store.
     * @param s is the error message.
     */
    public final void error(final String s) {
        logger.error("[FaultInstanceId = " + faultInstanceId + "]\t[FaultId = "
                + faultId + "]\t"
                + "[FaultName = "
                + className + "]\t" + s);
    }

    /**
     * This function can log any error exception message you want to store.
     * @param e is the error exception.
     */
    public final void error(final Exception e) {
        logger.error("[FaultInstanceId = " + faultInstanceId + "]\t[FaultId = "
                + faultId + "]\t"
                + "[FaultName = "
                + className + "]\t" + e);
    }

}
