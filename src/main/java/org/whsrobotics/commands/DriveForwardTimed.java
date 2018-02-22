package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardTimed extends TimedCommand{

    public DriveForwardTimed(double timeout) {
        super(new DriveForward(), timeout);
    }

}
