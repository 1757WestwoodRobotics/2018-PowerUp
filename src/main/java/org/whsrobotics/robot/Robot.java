package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.subsystems.Led;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.utils.RobotLogger;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        RobotLogger.log(this.getClass(), "Starting robot");

        OI.getInstance();
        DriveTrain.getInstance();
        Elevator.getInstance();
        Led.getInstance();
        Led led = Led.getInstance();
        led.On();

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
        Led led = Led.getInstance();
        Scheduler.getInstance().run();
        led.On();
        Timer.delay(1);
        led.Off();
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
