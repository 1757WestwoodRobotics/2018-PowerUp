package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;

public class TimedCommand extends edu.wpi.first.wpilibj.command.TimedCommand {

    private Command command;

    public TimedCommand(Command command, double timeout) {
        super(timeout);
    }

    @Override
    protected void execute() {
        command.start();
    }

    @Override
    protected void end() {
        command.cancel();
    }

}
