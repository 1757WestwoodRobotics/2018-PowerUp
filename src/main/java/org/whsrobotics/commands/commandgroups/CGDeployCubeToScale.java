package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.commands.TimedCommand;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToScale extends CommandGroup {

    public CGDeployCubeToScale(){

        addSequential(new MoveElevatorPosition(Elevator.Position.SCALE_TOP));
        // addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));     TODO: Fix Later
        addSequential(new TimedCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS),3));  // TODO: Sensor based
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}