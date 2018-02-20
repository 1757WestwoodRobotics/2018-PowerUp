package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.MoveCubeGripper;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    public CGGrabCube() {

            // Move the CubeGripper arms to the RECEIVE_CUBE position
        addSequential(new MoveCubeGripper(CubeGripper.Position.RECEIVE_CUBE));
            // Spin the CubeSpinner motors in the INTAKE mode (for 3 seconds) // TODO: Until sensor triggered
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS));
            // Tighten the CubeGripper arms with CubeGripper.applyConstantVoltage()
        addSequential(CubeGripper.applyConstantVoltageCommand);
            // Stop spinning the CubeSpinner motors
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

    }

}
