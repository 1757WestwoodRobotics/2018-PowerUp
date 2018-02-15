package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorVelocity extends Command {

    public MoveElevatorVelocity() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void execute() {

        if (OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) > 0) {
            Elevator.moveWithVelocity(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight));

        } else if (OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) > 0) {
            Elevator.moveWithVelocity(-OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft));
        }

    }

    @Override
    protected void end() {
        Elevator.moveWithVelocity(0);
    }

    @Override
    protected boolean isFinished() {
        return OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) == 0 &&
                OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) == 0;
    }

}
