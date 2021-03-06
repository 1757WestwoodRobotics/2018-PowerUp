package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class CGDeployCubeToScale extends CommandGroup {

    public CGDeployCubeToScale(){

        addSequential(new MoveElevatorPosition(Elevator.Position.SCALE_TOP));
        addSequential(new Command() {
            @Override
            protected boolean isFinished() {
                return Elevator.reachedTargetRange(Elevator.Position.SCALE_TOP.getTarget());
            }
        });

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the ultrasonic sensor), and open arms
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));

        addSequential(new TimedCommand(2));

        addSequential(new MoveCubeGripper(CubeGripper.Position.OPEN_ARMS));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}