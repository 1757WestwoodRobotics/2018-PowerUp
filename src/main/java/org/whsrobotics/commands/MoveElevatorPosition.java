package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorPosition extends InstantCommand {

    private Elevator.Position position;

    public MoveElevatorPosition(Elevator.Position position) {
        requires(Elevator.getInstance());

        this.position = position;
    }

    @Override
    protected void execute() {
        Elevator.moveToPosition(position);
    }

}
