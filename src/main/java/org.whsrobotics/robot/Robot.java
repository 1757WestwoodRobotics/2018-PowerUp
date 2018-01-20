package org.whsrobotics.robot;


public class Robot extends IterativeRobot {

    /**
     * Robot-wide initialization code should go here.
     * <p>
     * <p>Users should override this method for default Robot-wide initialization which will be called
     * when the robot is first powered on. It will be called exactly one time.
     * <p>
     * <p>Warning: the Driver Station "Robot Code" light and FMS "Robot Ready" indicators will be off
     * until RobotInit() exits. Code in RobotInit() that waits for enable will cause the robot to
     * never indicate that the code is ready, causing the robot to be bypassed in a match.
     */
    @Override
    public void robotInit() {
    }

    /**
     * Initialization code for disabled mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    /**
     * Initialization code for teleop mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters teleop mode.
     */
    @Override
    public void teleopInit() {
    }

    /**
     * Periodic code for all robot modes should go here.
     * <p>
     * <p>This function is called each time a new packet is received from the driver station.
     * <p>
     * <p>Packets are received approximately every 20ms.  Fixed loop timing is not guaranteed due to
     * network timing variability and the function may not be called at all if the Driver Station is
     * disconnected.  For most use cases the variable timing will not be an issue.  If your code does
     * require guaranteed fixed periodic timing, consider using Notifier or PIDController instead.
     */
    @Override
    public void robotPeriodic() {
    }

    /**
     * Periodic code for disabled mode should go here.
     * <p>
     * <p>Users should override this method for code which will be called each time a new packet is
     * received from the driver station and the robot is in disabled mode.
     * <p>
     * <p>Packets are received approximately every 20ms.  Fixed loop timing is not guaranteed due to
     * network timing variability and the function may not be called at all if the Driver Station is
     * disconnected.  For most use cases the variable timing will not be an issue.  If your code does
     * require guaranteed fixed periodic timing, consider using Notifier or PIDController instead.
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * Periodic code for teleop mode should go here.
     * <p>
     * <p>Users should override this method for code which will be called each time a new packet is
     * received from the driver station and the robot is in teleop mode.
     * <p>
     * <p>Packets are received approximately every 20ms.  Fixed loop timing is not guaranteed due to
     * network timing variability and the function may not be called at all if the Driver Station is
     * disconnected.  For most use cases the variable timing will not be an issue.  If your code does
     * require guaranteed fixed periodic timing, consider using Notifier or PIDController instead.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
