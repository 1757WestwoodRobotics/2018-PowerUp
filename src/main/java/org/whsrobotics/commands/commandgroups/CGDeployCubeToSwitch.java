package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.commands.TimedCommand;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToSwitch extends CommandGroup{

    public CGDeployCubeToSwitch(){

        //Brings Elevator to the Middle
        addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));
        //Spins Cube Outwards
        addSequential (new TimedCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS),3));
        //Release CubeGripper
        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));

    }
}
