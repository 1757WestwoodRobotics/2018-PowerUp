package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorDS extends InstantCommand {

    public MoveElevatorDS() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void execute() {
        (new MoveElevatorPosition(OI.getSelectedElevatorPosition())).start();
    }

}
