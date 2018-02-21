package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.subsystems.*;
import org.whsrobotics.utils.RobotLogger;

/**
 * High-level class that performs actions based on defined competition periods. Extends WPILib's TimedRobot.
 *
 * @author Larry Tseng
 * @see edu.wpi.first.wpilibj.TimedRobot
 */
public class Robot extends TimedRobot {

    /**
     * Called once when the robot program has started. Initializes all of the subsystems as necessary.
     */
    @Override
    public void robotInit() {
        RobotLogger.log(this.getClass(), "Starting robot");

        OI.getInstance();

        DriveTrain.getInstance();
        Elevator.getInstance();

        CubeGripper.getInstance();
        CubeSpinner.getInstance();

        //  Arduino.getInstance();
    }

    // ------------ AUTONOMOUS METHODS ------------- //

    /**
     * Called once at the beginning of the autonomous period.
     */
    @Override
    public void autonomousInit() {
        RobotLogger.log(this.getClass(), "Starting autonomous");

        // TODO: Create an autonomous class that holds the code for auto init.
    }

    /**
     * Called every ~20ms during the autonomous period.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    // ------------ TELEOPERATED METHODS ------------- //

    /**
     * Called once at the beginning of the teleoperated period.
     */
    @Override
    public void teleopInit() {
        RobotLogger.log(this.getClass(), "Starting teleop");
    }

    /**
     * Called every ~20ms during the teleoperated period.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    // ------------ DISABLED METHODS ------------- //

    /**
     * Called once every time the robot is disabled. Use it to reset sensors/motors/etc.
     */
    @Override
    public void disabledInit() {
        // Any time robot goes into disabled (run once)
        RobotLogger.log(this.getClass(), "Entering disabled");
    }

//    @Override
//    public void disabledPeriodic() {
//    }

}
