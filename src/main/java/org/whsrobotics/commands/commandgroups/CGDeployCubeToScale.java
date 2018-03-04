package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.CubeNotInArms;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

public class CGDeployCubeToScale extends CommandGroup {

    public CGDeployCubeToScale(){

        addSequential(new MoveElevatorPosition(Elevator.Position.SCALE_TOP));

        addSequential(new Command() {
            @Override
            protected boolean isFinished() {
                double max = Elevator.Position.SCALE_TOP.getTarget() + 600;
                double min = Elevator.Position.SCALE_TOP.getTarget() - 600;
                double current = Elevator.getEncoderPosition();
                return current > min && current < max;
            }
        });

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the ultrasonic sensor), and open arms
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));

        addSequential(new TimedCommand(2));

        addSequential(new MoveCubeGripper(CubeGripper.Position.GRAB_CUBE));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}