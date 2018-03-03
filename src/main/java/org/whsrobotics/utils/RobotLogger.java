package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import org.whsrobotics.subsystems.Arduino;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RobotLogger {

    static {
        try {
            FileHandler fh = new FileHandler("/home/lvuser/logs/frc1757-" +
                    new SimpleDateFormat("yyyy.MM.dd-hh:mm").format(new Date()));

            fh.setFormatter(new SimpleFormatter());
            Logger.getLogger("Robot").addHandler(fh);
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

    /**
     * Logs a message to the Robot Logger with the current Robot time.
     *
     * @param name the class calling the logger
     * @param level the severity of the message
     * @param message the message to log
     */
    private void log(Class name, Level level, String message) {
        Logger.getLogger("Robot").log(level, "" + Timer.getFPGATimestamp() + name + ": " + message);
    }

    public void log(Class name, String message) {
        log(name, Level.INFO, message);
    }

    public void err(Class name, String message, boolean printTrace) {
        DriverStation.reportError(message, printTrace);
        log(name, Level.SEVERE, message);

        Arduino.getInstance().Send(Arduino.Command.StripLEDsWhite); // Visual indicator for robot failure

    }

}