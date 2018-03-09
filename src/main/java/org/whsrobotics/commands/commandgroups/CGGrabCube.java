package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.Arduino;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.utils.AutomationCancelerHelper;

/**
 * Sequence of commands to grab a Power Cube. Opens the robot arms and spins the intake wheels.
 */
public class CGGrabCube extends CommandGroup {

    private final Elevator.Position position = Elevator.Position.DOWN;

    public CGGrabCube() {

        requires(new AutomationCancelerHelper());

        // Move the Elevator to the down position. If it can't do it in 3 seconds, stop it.
//        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
//        addSequential(new Command() {
//            @Override
//            protected boolean isFinished() {
//                return Elevator.reachedTarget(Elevator.Position.DOWN.getTarget());
//            }
//        });

        // TODO: TEST
        addSequential(new ExecuteCommandWithFinishable(new MoveElevatorPosition(position),
                () -> Elevator.reachedTarget(position)), 5);    // If it doesn't get there in 3 seconds, move on.

        // Move the CubeGripper arms to the OPEN_ARMS position
        addSequential(new MoveCubeGripper(CubeGripper.Position.OPEN_ARMS), 3); // Maximum 3 seconds

        // Wait until Cube is detected with the sensor (40 cm)
        addSequential(new ArduinoUltrasonicDistance(40));

        // Move the CubeGripper arms to the GRAB_CUBE position
        addSequential(new MoveCubeGripper(CubeGripper.Position.GRAB_CUBE), 3); // Maximum 3 seconds

        // Spin the CubeSpinner motors in the INTAKE mode (until Cube is detected with the sensor), and bring arms closer
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS));

        // Wait until Cube is detected with the sensor (25 cm)
        addSequential(new ArduinoUltrasonicDistance(25));   // TODO: Measure bumpers AND test
        addSequential(new ArduinoSendCommand(Arduino.Command.StripLEDsOrange)); // TODO: Pulse as well

//        // Revert to this if the thread separation doesn't work!!!!!
//        addSequential(new Command() {
//
//            @Override
//            protected boolean isFinished() {
//                double output = Arduino.getInstance().getDistance(); // --> replace ArduinoUltrasonicCommand
//                return output < 20 && output != -1;
//            }
//
//        });

        // Grasp the Cube tightly
        addParallel(new CubeGripperApplyConstantVoltage());

        // Turn off the spinners
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

    }
}
