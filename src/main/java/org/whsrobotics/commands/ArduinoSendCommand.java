package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.subsystems.Arduino;

public class ArduinoSendCommand extends InstantCommand {

    public ArduinoSendCommand(Arduino.Command command) {
        Arduino.getInstance().Send(command);
    }

}
