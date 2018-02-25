package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.commands.TimedCommand;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToExchange extends CommandGroup {

    public CGDeployCubeToExchange(){
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
        addSequential (new TimedCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS),3));
        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));
     //Elevator Down Spin Outwards Open Arms
    }
}
