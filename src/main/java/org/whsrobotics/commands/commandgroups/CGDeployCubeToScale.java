package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToScale extends CommandGroup {

public CGDeployCubeToScale(){

    addSequential(new MoveElevatorPosition(Elevator.Position.UP));
   // addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));     TODO: Fix Later
    addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));  // TODO: Time/Sensor based
    addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
    }

}