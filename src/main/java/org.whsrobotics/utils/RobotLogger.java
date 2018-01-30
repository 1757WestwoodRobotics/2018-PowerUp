package org.whsrobotics.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger extends Logger {

    private static FileHandler fileHandler = null;

    private static RobotLogger instance;

    public static RobotLogger getInstance() {
        if (instance == null) {
            instance = new RobotLogger();
        }

        return instance;
    }

    private RobotLogger() {

        super(null, null);

        try {
            fileHandler = new FileHandler("/home/lvuser/logs/frc1757-robotlog-" + LocalDateTime.now() + "%u");
            this.addHandler(fileHandler);
        } catch (IOException e) {
            this.log(Level.SEVERE, e.getMessage());
        }

    }

    public void log(String name, Level level, String message) {
        Logger logger = RobotLogger.getLogger(name);
        logger.log(level, message);

    }

}
