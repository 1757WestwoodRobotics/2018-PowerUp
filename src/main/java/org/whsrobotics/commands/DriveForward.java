package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.RobotLogger;
import org.whsrobotics.utils.Units;

public class DriveForward extends Command {

    private double meters = 0.0;

    public DriveForward(double distance, Units unit) {
        requires(DriveTrain.getInstance());

        this.meters = Units.convert(unit, Units.METERS, distance);
    }

    @Override
    protected void initialize() {
        DriveTrain.initializeLeftPositionPIDController();
        DriveTrain.initializeRightPositionPIDController();
        DriveTrain.setPositionPIDInMeters(meters);
        RobotLogger.getInstance().log(this.getClass(), "About to drive forward: " + meters + "meters");
        DriveTrain.enablePositionPIDControllers();
    }

    @Override
    protected void execute() {
        System.out.println(DriveTrain.getLeftPositionPIDOutput() + " " + DriveTrain.getRightPositionPIDOutput());
    }

    @Override
    protected void end() {
        RobotLogger.getInstance().log(this.getClass(), "Finished driving forwards");
        DriveTrain.disablePositionPIDControllers();
    }

    @Override
    protected boolean isFinished() {
       // return DriveTrain.arePositionPIDControllersOnTarget();
        return false;
    }

}
