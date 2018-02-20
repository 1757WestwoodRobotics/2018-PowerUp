package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    public CGGrabCube() {

        // Move the CubeGripper arms to the RECEIVE_CUBE position

        // Spin the CubeSpinner motors in the INTAKE mode (for 3 seconds) // TODO: Until sensor triggered

        // Tighten the CubeGripper arms with CubeGripper.applyConstantVoltage()

        // Stop spinning the CubeSpinner motors

    }

}
