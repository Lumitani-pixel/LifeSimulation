package net.normalv.lifesimulation.managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {
    private File folder = new File("logs");
    private File log;
    private FileWriter writer;

    public LogManager() {
        int logFileCount = 1;
        if (!folder.exists()) folder.mkdirs();

        // Make a new log
        log = new File(folder, "log"+logFileCount+".txt");
        while (log.exists()) {
            logFileCount++;
            log = new File(folder, "log"+logFileCount+".txt");
        }

        // Make a new Instance of the File Writer
        try {
            log.createNewFile();
            writer = new FileWriter(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void log(String log) {
        try {
            writer.write(log+"\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
