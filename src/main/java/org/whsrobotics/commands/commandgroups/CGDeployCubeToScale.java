package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.CubeNotInArms;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

public class CGDeployCubeToScale extends CommandGroup {

    public CGDeployCubeToScale(){

        // addSequential(new MoveElevatorPosition(Elevator.Position.SCALE_TOP));

        // Move the Elevator to the highest position. If it can't do it in 5 seconds, stop it.
        addSequential(new FinishWithTriggerCommand(new MoveElevatorPosition(Elevator.Position.SCALE_TOP), new ElevatorHasReachedSetpoint()), 5);

        // Hard-coded time-based delay
        addSequential(new TimedCommand(0.25));

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the IR sensor), and open arms TODO: TEST
        addSequential(new FinishWithTriggerCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS), new CubeNotInArms()), 3);

        addSequential(new MoveCubeGripper(CubeGripper.Position.GRAB_CUBE));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}