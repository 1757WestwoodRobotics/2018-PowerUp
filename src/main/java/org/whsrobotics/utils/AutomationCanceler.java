package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.command.Command;

public class AutomationCanceler extends Command {

    public AutomationCanceler() {
        requires(new AutomationCancelerHelper());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
