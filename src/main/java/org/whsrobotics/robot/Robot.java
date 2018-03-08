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
        RobotLogger.getInstance().log(this.getClass(), "Starting robot");

        DriveTrain.getInstance();
        Elevator.getInstance();
        CubeGripper.getInstance();
        CubeSpinner.getInstance();

        Arduino.getInstance();
        Vision.getInstance();

        Autonomous.getInstance();
        OI.getInstance();

    }

    // ------------ AUTONOMOUS METHODS ------------- //

    /**
     * Called once at the beginning of the autonomous period.
     */
    @Override
    public void autonomousInit() {
        RobotLogger.getInstance().log(this.getClass(), "Starting autonomous");
        Autonomous.startInit();
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
        RobotLogger.getInstance().log(this.getClass(), "Starting teleop");
    }

    /**
     * Called every ~20ms during the teleoperated period.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();

//      // Temporary code for testing Arduino communication
//      for (Arduino.Command command : Arduino.Command.values()) {
//            Arduino.getInstance().Send(command);
//            Timer.delay(1);
//        }
//
        // System.out.println(new String("Ultrasonic sensor value - ") + Arduino.getInstance().getDistance());

        System.out.println("left count: " + DriveTrain.getLeftEncoderCount() +
                " distance (cm): " + DriveTrain.getLeftEncoderDistance() +
                " rate (cm/s): " + DriveTrain.getLeftEncoderRate());

    }

    // ------------ DISABLED METHODS ------------- //

    /**
     * Called once every time the robot is disabled. Use it to reset sensors/motors/etc.
     */
    @Override
    public void disabledInit() {
        // Any time robot goes into disabled (run once) [And RobotInit finishes]
        RobotLogger.getInstance().log(this.getClass(), "Entering disabled");
        Scheduler.getInstance().removeAll();

        CubeSpinner.spinWithMode(CubeSpinner.Mode.OFF);
        CubeGripper.setTalonNeutral();

        Arduino.getInstance().onDisabledInit();

    }

}
