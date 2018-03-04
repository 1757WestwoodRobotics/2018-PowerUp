package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.subsystems.Arduino;

public class CubeUltrasonic extends Trigger {

    @Override
    public boolean get() {
        double distance = Arduino.getInstance().getDistance();
        return distance < 30; // Constant for the distance to cube
    }

}
