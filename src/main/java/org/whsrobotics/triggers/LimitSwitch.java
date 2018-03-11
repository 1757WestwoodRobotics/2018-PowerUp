package org.whsrobotics.triggers;

import edu.wpi.first.wpilibj.DigitalInput;
import org.whsrobotics.utils.RobotLogger;

public class LimitSwitch {

    private DigitalInput limitSwitch;

    public LimitSwitch(int port) {
        limitSwitch = new DigitalInput(port);
    }

    public boolean get() {
        try {
            return !limitSwitch.get();
        } catch (Exception ex) {
            RobotLogger.getInstance().err(this.getClass(), "Error getting a limit switch!", true);
            throw ex;
        }

    }
    
}
