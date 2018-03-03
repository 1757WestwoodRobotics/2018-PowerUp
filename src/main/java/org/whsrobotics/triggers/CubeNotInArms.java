package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.subsystems.CubeSpinner;

public class CubeNotInArms extends Trigger {

    @Override
    public boolean get() {
        return !CubeSpinner.isCubePresent();
    }

}
