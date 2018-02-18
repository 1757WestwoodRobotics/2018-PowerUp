package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Elevator;

public class MoveElevatorDS extends InstantCommand {

    private static MoveElevatorDS instance;

    private MoveElevatorDS() {
        requires(Elevator.getInstance());
    }

    public static MoveElevatorDS getInstance() {
        if (instance == null) {
            instance = new MoveElevatorDS();
        }

        return instance;
    }

    @Override
    protected void execute() {
        Elevator.moveToDS(OI.getManualTargetElevatorPosition());
    }

}
