package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.robot.OI;

public class ElevatorVelocityMode extends Trigger {

    @Override
    public boolean get() {
        return OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) > 0 ||
                OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) > 0;
    }

}
