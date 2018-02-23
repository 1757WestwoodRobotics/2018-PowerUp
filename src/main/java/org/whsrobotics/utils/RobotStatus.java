package org.whsrobotics.utils;

import org.whsrobotics.subsystems.DriveTrain;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A class to diagnose status of hardware and connections. Creates a new NetworkTables table to store results. Called every ## seconds.
 */
public class RobotStatus {

    // TODO: Convert to singleton

    private static Timer timer;
    private static final int PERIOD = 1000;    // loop period in milliseconds (1 s = 1000 ms)

    private static TimerTask task = new TimerTask() {
        @Override
        public void run() {

        }
    };

    private RobotStatus() {
        timer = new Timer("RobotStatusLoop");
        timer.schedule(task, 0, PERIOD);
    }

}
