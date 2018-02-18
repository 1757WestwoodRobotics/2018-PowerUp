package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorPosition extends InstantCommand {

    private static MoveElevatorPosition instance;

    private MoveElevatorPosition() {
        requires(Elevator.getInstance());
    }

    public static MoveElevatorPosition getInstance() {
        if (instance == null) {
            instance = new MoveElevatorPosition();
        }

        return instance;
    }

    @Override
    protected void end() {
        Elevator.moveToPosition(OI.getSelectedElevatorPosition());
    }

}
