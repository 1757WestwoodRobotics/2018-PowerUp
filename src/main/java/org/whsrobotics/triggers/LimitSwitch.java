package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {

    private DigitalInput limitSwitch;

    public LimitSwitch(int port) {
        limitSwitch = new DigitalInput(port);
    }

    public boolean get() {
        return limitSwitch.get();
    }
    
}
