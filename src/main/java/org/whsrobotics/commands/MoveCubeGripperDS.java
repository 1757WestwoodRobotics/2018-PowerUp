package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.Elevator;

public class MoveCubeGripperDS extends InstantCommand {

    public MoveCubeGripperDS() {

    }

    @Override
    protected void execute() {
        (new MoveCubeGripper(OI.getSelectedCubeGripperPosition())).start();

    }

}
