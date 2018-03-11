package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorVelocity extends Command {

    public MoveElevatorVelocity() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize() {
        Elevator.setEndgameVoltageLimits();
    }

    @Override
    protected void execute() {

        if (OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight) > 0.05) {
            Elevator.moveWithVelocity(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight));

        } else if (OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft) > 0.05) {
            Elevator.moveWithVelocity(-OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft));
        }

    }

    @Override
    protected void end() {
        Elevator.setNormalVoltageLimits();
        Elevator.moveToValue(Elevator.getEncoderPosition());   // Hold the current position
    }

    @Override
    protected boolean isFinished() {
        return OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft) < 0.05 &&
                OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight) < 0.05;
    }

}
