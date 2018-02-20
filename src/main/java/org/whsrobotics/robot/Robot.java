package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.subsystems.*;
import org.whsrobotics.utils.RobotLogger;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        RobotLogger.log(this.getClass(), "Starting robot");

      //  OI.getInstance();
      //  DriveTrain.getInstance();
      //  Elevator.getInstance();
      //  Arduino.getInstance();

        OI.getInstance();
        CubeGripper.getInstance();
        CubeSpinner.getInstance();
    }

    // ------------ AUTONOMOUS METHODS ------------- //

    @Override
    public void autonomousInit() {
        RobotLogger.log(this.getClass(), "Starting autonomous");
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    // ------------ TELEOPERATED METHODS ------------- //

    @Override
    public void teleopInit() {
        RobotLogger.log(this.getClass(), "Starting teleop");
    }

    @Override
    public void teleopPeriodic() {

        Scheduler.getInstance().run();
//
//        Arduino uno = Arduino.getInstance(); // For Testing only
//
//        uno.ledCommand(Arduino.LED_ON);
//        Timer.delay(1);
//        uno.ledCommand(Arduino.REFL_TAPE);
//        Timer.delay(1);
//        uno.ledCommand(Arduino.FIND_BOX);
//        Timer.delay(1);
//        uno.ledCommand(Arduino.LED_OFF);
//        Timer.delay(1);
    }

    // ------------ DISABLED METHODS ------------- //

    @Override
    public void disabledInit() {
        // Any time robot goes into disabled (run once)
        RobotLogger.log(this.getClass(), "Entering disabled");
    }

    @Override
    public void disabledPeriodic() {
    }

}
