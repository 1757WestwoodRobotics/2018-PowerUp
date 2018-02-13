package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class ElevatorTopLimit extends Trigger {

    @Override
    public boolean get() {
        return false;
        // return true if sensor has been triggered. since command called?
    }
}
