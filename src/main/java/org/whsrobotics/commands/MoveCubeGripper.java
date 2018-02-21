package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.subsystems.CubeGripper;

public class MoveCubeGripper extends InstantCommand {

    private CubeGripper.Position position;

    public MoveCubeGripper(CubeGripper.Position position) {
        requires(CubeGripper.getInstance());

        this.position = position;
    }

    @Override
    protected void execute() {
        CubeGripper.moveToPosition(position);
    }

}
