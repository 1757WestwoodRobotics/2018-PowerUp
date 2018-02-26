package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.DriverStation;
import org.whsrobotics.robot.Robot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotLogger {

    static {
        try {
            Logger.getLogger("Robot").addHandler(new FileHandler("/home/lvuser/logs/frc1757-" + LocalDateTime.now()));
        } catch (IOException e) {
            System.err.println("Error adding file handler to Logger. Did you make a directory called 'logs' under '/home/lvuser'?");
        }

    }

    private static RobotLogger instance;

    private RobotLogger() {

    }

    public static RobotLogger getInstance() {
        if (instance == null) {
            instance = new RobotLogger();
        }

        return instance;
    }

    private void log(Class name, Level level, String message) {
        Logger.getLogger("Robot").log(level, name + ": " + message);

    }

    public void log(Class name, String message) {
        log(name, Level.INFO, message);
    }

    public void err(Class name, String message) {
        DriverStation.reportError(message, true);
        log(name, Level.SEVERE, message);
    }

}