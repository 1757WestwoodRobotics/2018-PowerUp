package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.CubeUltrasonic;
import org.whsrobotics.triggers.ElevatorHasReachedSetpoint;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    public CGGrabCube() {

        // Move the Elevator to the down position. If it can't do it in 5 seconds, stop it.
        addSequential(new FinishWithTriggerCommand(
                new MoveElevatorPosition(Elevator.Position.DOWN),
                new ElevatorHasReachedSetpoint()), 5);

        // Move the CubeGripper arms to the GRAB_CUBE position
        addSequential(new MoveCubeGripper(CubeGripper.Position.GRAB_CUBE), 3); // Maximum 3 seconds

        // Spin the CubeSpinner motors in the INTAKE mode (until Cube is detected with the IR sensor), and bring arms closer
        addParallel(new MoveCubeGripper(CubeGripper.Position.CLOSE_ARMS));

        addParallel(new FinishWithTriggerCommand(
                new SpinCubeSpinner(CubeSpinner.Mode.INWARDS),
                new CubeUltrasonic()), 5);    // Maximum 5 seconds

        // Grasp the Cube tightly
        addSequential(new CubeGripperApplyConstantVoltage());

        // Turn off the spinners
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

    }
}
