package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;
import org.whsrobotics.subsystems.DriveTrain;

public class DriveForward extends TimedCommand {

    public DriveForward(double timeout) {
        super(timeout);
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void execute() {
        DriveTrain.controllerDrive(.5, 0);
    }

    @Override
    protected void end() {
        DriveTrain.stopDrive();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

}
