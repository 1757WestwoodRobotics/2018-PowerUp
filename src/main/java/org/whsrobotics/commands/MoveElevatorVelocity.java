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
    protected void execute() {

        if (OI.leftXboxJoystickCurve(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) > 0 && !Elevator.getTopLimitSwitch()) {
            Elevator.moveWithVelocity(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight));

        } else if (OI.leftXboxJoystickCurve(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) > 0 && !Elevator.getBottomLimitSwitch()) {
            Elevator.moveWithVelocity(-OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft));
        }

    }

    @Override
    protected void end() {
        Elevator.moveToValue(Elevator.getEncoderPosition());   // Hold the current position
        // Elevator.moveWithVelocity(0);
    }

    @Override
    protected boolean isFinished() {
        return OI.leftXboxJoystickCurve(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) == 0 &&
                OI.leftXboxJoystickCurve(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) == 0;
    }

}
