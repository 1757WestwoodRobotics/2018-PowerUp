package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.whsrobotics.commands.DriveForwardTimed;
import org.whsrobotics.commands.TurnToAngle;



public class CGPathTest extends CommandGroup {

    public CGPathTest() {

        addSequential(new DriveForwardTimed(3));
        addSequential(new TurnToAngle(-90));
        addSequential(new DriveForwardTimed(1));
        addSequential(new TurnToAngle(0));
        addSequential(new DriveForwardTimed(3));
    }
}
