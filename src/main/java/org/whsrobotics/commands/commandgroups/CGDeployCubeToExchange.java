package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

public class CGDeployCubeToExchange extends CommandGroup {

    public CGDeployCubeToExchange(){

        // addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

        // Move the Elevator to the down position. If it can't do it in 5 seconds, stop it.
        addSequential(new FinishWithTriggerCommand(new MoveElevatorPosition(Elevator.Position.DOWN), new ElevatorHasReachedSetpoint()), 5);

        // Hard-coded time-based delay
        addSequential(new TimedCommand(0.25));

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the IR sensor), and open arms TODO: TEST
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS), 3);

        addSequential(new MoveCubeGripper(CubeGripper.Position.CLOSE_ARMS));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

    }
}
