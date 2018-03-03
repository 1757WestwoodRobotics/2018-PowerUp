package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    public CGGrabCube() {

//        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
        // Move the Elevator to the down position. If it can't do it in 5 seconds, stop it.
        addSequential(new WaitForTriggerCommand(new MoveElevatorPosition(Elevator.Position.DOWN), new ElevatorHasReachedSetpoint()), 5);

        // Move the CubeGripper arms to a position
        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE));

        // hard-coded time-based delay before the arms close
        addSequential(new TimedCommand(1));

        // Spin the CubeSpinner motors in the INTAKE mode (until Cube is detected with the IR sensor), and bring arms closer
        addParallel(new WaitForTriggerCommand(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS), CubeSpinner.getIRSensor()));
        addParallel(new MoveCubeGripper(CubeGripper.Position.CLOSE_ARMS));

        // Turn off the spinner motors
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

        // Grasp the Cube tightly (will timeout after 60s)
        addSequential(new CubeGripperApplyConstantVoltage());

    }
}
