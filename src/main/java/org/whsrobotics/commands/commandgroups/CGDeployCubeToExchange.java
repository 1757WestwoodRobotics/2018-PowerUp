package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToExchange extends CommandGroup {

    public CGDeployCubeToExchange(){

        // addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

        // Move the Elevator to the down position (until it has reached target)
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
        addSequential(new Command() {
            @Override
            protected boolean isFinished() {
                return Elevator.reachedTargetRange(Elevator.Position.DOWN.getTarget());
            }
        });

        // Hard-coded time-based delay
        addSequential(new TimedCommand(0.25));

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the IR sensor), and open arms TODO: TEST
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS), 3);

        addSequential(new MoveCubeGripper(CubeGripper.Position.GRAB_CUBE));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

    }
}
