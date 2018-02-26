package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.commands.TimedCommand;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    public CGGrabCube() {

        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

            // Move the CubeGripper arms to the RECEIVE_CUBE position
        addSequential(new MoveCubeGripper(CubeGripper.Position.MIDDLE));
        addSequential(new TimedCommand(0.5));

        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));

            // Spin the CubeSpinner motors in the INTAKE mode (for 2 seconds) // TODO: Until sensor triggered
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS));
        addSequential(new TimedCommand(2));

        addSequential(new Command() {
            @Override
            protected boolean isFinished() {
                CubeGripper.applyConstantVoltage();
                return true;
            }
        });

        addSequential(new TimedCommand(0.5));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));


    }
}
