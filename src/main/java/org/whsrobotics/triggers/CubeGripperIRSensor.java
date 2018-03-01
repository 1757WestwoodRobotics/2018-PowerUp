package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class CubeGripperIRSensor extends Trigger {

    @Override
    public boolean get() {
        return false;
        // return if (CubeGripper.getIRSensor()) > ...
    }

}
