package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.DriveTrain;

public class DriveForward extends Command {

    public DriveForward() {
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
        return false;
    }

}
