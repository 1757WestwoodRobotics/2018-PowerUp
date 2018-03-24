package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.TimedCommand;
import org.whsrobotics.subsystems.DriveTrain;

public class DriveTimed extends TimedCommand {

    public DriveTimed(double timeout) {
        super(timeout);
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void execute() {
        DriveTrain.controllerDrive(.75, 0);
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