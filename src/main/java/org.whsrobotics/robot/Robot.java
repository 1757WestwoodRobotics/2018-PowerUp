package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.utils.RobotLogger;

import java.util.logging.Level;

public class Robot extends TimedRobot {

    RobotLogger robotLogger;
    OI oi;

    @Override
    public void robotInit() {
        robotLogger = RobotLogger.getInstance();
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering robotInit");

        oi = OI.getInstance();
    }

    // ------------ AUTONOMOUS METHODS ------------- //

    @Override
    public void autonomousInit() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering autonomousInit");
    }

    @Override
    public void autonomousPeriodic() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering autonomousPeriodic");
        Scheduler.getInstance().run();
    }

    // ------------ TELEOPERATED METHODS ------------- //

    @Override
    public void teleopInit() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering teleopInit");
    }

    @Override
    public void teleopPeriodic() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering teleopPeriodic");
        Scheduler.getInstance().run();
    }

    // ------------ DISABLED METHODS ------------- //

    @Override
    public void disabledInit() {
        // Any time robot goes into disabled (run once)
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering disabledInit");
    }

    @Override
    public void disabledPeriodic() {
        // Do repeatedly while disabled (run repeatedly)
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering disabledPeriodic");
    }

}
