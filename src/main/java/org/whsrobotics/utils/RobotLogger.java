package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.DriverStation;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger {

    static {
        try {
            Logger.getLogger("").addHandler(new FileHandler("/home/lvuser/logs/frc1757-" + "%u"));
        } catch (IOException e) {
            System.err.println("Error adding file handler to Logger. Did you make a directory called 'logs' under '/home/lvuser'?");
        }

    }

    public static void log(Class name, Level level, String message) {
        Logger.getLogger(name.getName()).log(level, name + ": " + message);

    }

    public static void log(Class name, String message) {
        log(name, Level.INFO, message);
    }

    public static void err(Class name, String message) {
        DriverStation.reportError(message, true);
        log(name, Level.SEVERE, message);
    }

}