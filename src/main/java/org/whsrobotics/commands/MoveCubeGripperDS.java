package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.Elevator;

public class MoveCubeGripperDS extends InstantCommand {

    private static MoveCubeGripperDS instance;

    private MoveCubeGripperDS() {
        requires(CubeGripper.getInstance());
    }

    public static MoveCubeGripperDS getInstance() {
        if (instance == null) {
            instance = new MoveCubeGripperDS();
        }

        return instance;
    }

    @Override
    protected void execute() {
        CubeGripper.moveToDS(OI.getManualTargetCubeGripperPosition());
    }

}
