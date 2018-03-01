package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.RobotLogger;

public class WaitForTriggerCommand extends Command {

    private Command command;
    private Trigger trigger;

    public WaitForTriggerCommand(Command command, Trigger trigger) {
        this.command = command;
        this.trigger = trigger;
    }

    @Override
    protected void execute() {
        command.start();
    }

    @Override
    protected void end() {
        command.cancel();
    }

    @Override
    protected boolean isFinished() {
        try {
            return trigger.get();
        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error with getting trigger: " + trigger.getName());
            return false;
        }
    }

}
