package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;
import org.whsrobotics.subsystems.Elevator;

public class ElevatorHasReachedSetpoint extends Trigger {

    @Override
    public boolean get() {
        return Elevator.reachedTarget();
    }

}
