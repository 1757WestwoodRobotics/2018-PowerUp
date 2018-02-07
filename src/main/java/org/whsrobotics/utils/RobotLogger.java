package org.whsrobotics.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger {

    private Logger logger = Logger.getLogger("Robot");

    private static RobotLogger instance;

    public static RobotLogger getInstance() {
        if (instance == null) {
            instance = new RobotLogger();
        }

        return instance;
    }

    private RobotLogger() {
        try {
            logger.addHandler(new FileHandler("/home/lvuser/logs/frc1757-" + "%u"));
        } catch (IOException e) {
            System.err.println("Error adding file handler to Logger. Did you make a directory called 'logs' under '/home/lvuser'?");
        }

    }

    public void log(String name, Level level, String message) {
        logger.log(level, name + " " + message);

    }

}
