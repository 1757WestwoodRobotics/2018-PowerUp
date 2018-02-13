package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.DriveTrain;

public class DefaultDrive extends Command {

    public DefaultDrive() {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void initialize() {
        // Check if DriveTrain is initialized
    }

    @Override
    protected void execute() {
        DriveTrain.defaultDrive(OI.getXboxController().getY(GenericHID.Hand.kLeft),
                OI.getXboxController().getX(GenericHID.Hand.kRight));
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
