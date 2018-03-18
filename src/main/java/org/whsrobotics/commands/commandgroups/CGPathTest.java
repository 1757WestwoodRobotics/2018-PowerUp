package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.whsrobotics.commands.DriveForward;
import org.whsrobotics.commands.TurnToAngle;
import org.whsrobotics.utils.Units;


public class CGPathTest extends CommandGroup {

    public CGPathTest() {

        addSequential(new DriveForward(3, Units.FEET));
        addSequential(new TurnToAngle(-90));
        addSequential(new DriveForward(1, Units.METERS));
        addSequential(new TurnToAngle(0));
        addSequential(new DriveForward(7, Units.INCHES));

    }
}
