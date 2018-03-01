package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.CubeGripperIRSensor;

public class CGDeployCubeToSwitch extends CommandGroup{

    public CGDeployCubeToSwitch(){

        //Brings Elevator to the Middle
        addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));
        addSequential(new TimedCommand(2));

        //Spins Cube Outwards
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));
        addSequential(new TimedCommand(1));

        addSequential(new WaitForTriggerCommand(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS), CubeSpinner.getIRSensor()));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

        //Release CubeGripper
        addSequential(new MoveCubeGripper(CubeGripper.Position.MIDDLE));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }
}
