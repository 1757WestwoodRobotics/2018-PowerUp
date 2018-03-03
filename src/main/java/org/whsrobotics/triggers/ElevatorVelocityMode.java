package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.robot.OI;
import org.whsrobotics.utils.RobotLogger;

public class ElevatorVelocityMode extends Trigger {

    @Override
    public boolean get() {
        try {
            return OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kRight)) > 0 ||
                    OI.checkXboxDeadzone(OI.getXboxController().getTriggerAxis(GenericHID.Hand.kLeft)) > 0;
        } catch (Exception ex) {
            RobotLogger.getInstance().err(this.getClass(), "Error getting the xbox controller!", true);
            throw ex;
        }

    }

}
