package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.DriveTrain;

public class FlightStickDrive extends Command {

    public FlightStickDrive() {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void execute() {
        DriveTrain.flightStickDrive(OI.getFlightStickLeft().getY(GenericHID.Hand.kLeft),
                OI.getFlightStickRight().getY(GenericHID.Hand.kRight));
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