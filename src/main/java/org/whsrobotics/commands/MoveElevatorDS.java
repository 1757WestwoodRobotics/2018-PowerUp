package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorDS extends InstantCommand {

    private int target;

    public MoveElevatorDS(int target) {
        requires(Elevator.getInstance());

        this.target = target;
    }

    @Override
    protected void execute() {
        Elevator.moveToDS(target);
    }

}
