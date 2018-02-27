package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.subsystems.Arduino;

public class ArduinoSendCommand extends InstantCommand {

    private Arduino.Command command;

    public ArduinoSendCommand(Arduino.Command command) {
        requires(Arduino.getInstance());
        this.command = command;
    }

    @Override
    protected void initialize() {
        System.out.println("Sending arduino command");
        Arduino.getInstance().Send(command);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
