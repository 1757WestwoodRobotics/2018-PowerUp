package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.utils.RobotLogger;

import java.util.logging.Level;

public class Robot extends TimedRobot {

    private static OI oi;
    private static RobotLogger robotLogger;

    @Override
    public void robotInit() {
        robotLogger = RobotLogger.getInstance();
        robotLogger.log(this.getClass().getName(), Level.INFO, "Starting robotInit");

        oi = OI.getInstance();
    }

    // ------------ AUTONOMOUS METHODS ------------- //

    @Override
    public void autonomousInit() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Starting autonomousInit");
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    // ------------ TELEOPERATED METHODS ------------- //

    @Override
    public void teleopInit() {
        robotLogger.log(this.getClass().getName(), Level.INFO, "Entering teleopInit");
    }

    @Override
    public void teleopPeriodic() {
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
    }

}
