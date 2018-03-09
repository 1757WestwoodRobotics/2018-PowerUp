package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;

/**
 * DO NOT USE WITH INSTANTCOMMANDS!!! (It times out immediately regardless of timeout)
 *
 * Great for creating delays as a command
 */
public class TimedCommand extends edu.wpi.first.wpilibj.command.TimedCommand {

    private Command command;

    public TimedCommand(double timeout) {
        super(timeout);
    }

    public TimedCommand(Command command, double timeout) {
        super(timeout);
        this.command = command;
    }

    @Override
    protected void execute() {
        if (command != null)
            command.start();
    }

    @Override
    protected void end() {
        if (command != null)
            command.cancel();
        System.out.println("Ending TimedCommand");
    }

    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }
}
