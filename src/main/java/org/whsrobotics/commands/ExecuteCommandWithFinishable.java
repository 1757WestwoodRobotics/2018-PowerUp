package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.Finishable;

public class ExecuteCommandWithFinishable extends Command {

    private Command command;
    private Finishable finishable;

    public ExecuteCommandWithFinishable(Command command, Finishable finishable) {
        this.command = command;
        this.finishable = finishable;
    }

    @Override
    protected void initialize() {
        command.start();
    }

    @Override
    protected void execute() {
        if (command.isCompleted()) {
            command.start();    // Called every ~20ms to ensure that InstantCommands continues to run even if completed
        }
    }

    @Override
    protected boolean isFinished() {
        if (finishable.isFinished()) {
            command.cancel();
            return true;
        }
        return false;
    }

}
