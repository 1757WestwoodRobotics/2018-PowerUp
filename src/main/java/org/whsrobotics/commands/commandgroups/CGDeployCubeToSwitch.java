package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.CubeNotInArms;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

public class CGDeployCubeToSwitch extends CommandGroup{

    public CGDeployCubeToSwitch(){

        // addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));

        // Move the Elevator to the SWITCH position. If it can't do it in 5 seconds, stop it.
        addSequential(new WaitForTriggerCommand(new MoveElevatorPosition(Elevator.Position.SWITCH), new ElevatorHasReachedSetpoint()), 5);

        // Hard-coded time-based delay
        addSequential(new TimedCommand(0.25));

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the IR sensor), and open arms TODO: TEST
        addParallel(new WaitForTriggerCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS), new CubeNotInArms()));

        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }
}
