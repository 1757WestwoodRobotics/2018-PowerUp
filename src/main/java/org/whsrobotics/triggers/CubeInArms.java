package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.subsystems.CubeSpinner;

public class CubeInArms extends Trigger {

    @Override
    public boolean get() {
        return CubeSpinner.isCubePresent();
    }

}
