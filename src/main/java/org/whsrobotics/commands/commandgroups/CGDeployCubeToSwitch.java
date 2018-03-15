package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.utils.AutomationCancelerHelper;

public class CGDeployCubeToSwitch extends CommandGroup {

    // Specification:
    // Moves the Elevator to the SWITCH position
    // Spins the cube out of the arms
    // Stops spinning
    // Moves the arm back to the ALMOST_FOLD position (prevents hitting the switch)
    // Moves the Elevator back to the DOWN position

    private final Elevator.Position position = Elevator.Position.SWITCH;

    public CGDeployCubeToSwitch() {

        requires(new AutomationCancelerHelper());

        addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));
//        THIS CODE WORKS
//        addSequential(new Command() {
//            @Override
//            protected boolean isFinished() {
//                return Elevator.reachedTargetRange(Elevator.Position.SWITCH.getTarget());
//            }
//        });

        // TODO: TEST
        addSequential(new ExecuteCommandWithFinishable(new MoveElevatorPosition(position),
                () -> Elevator.reachedTargetRange(position)), 8);    // If it doesn't get there in 3 seconds, move on.

        // Spin the CubeSpinner motors in the OUTWARDS mode
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));

        addSequential(new TimedCommand(2));

        addSequential(new MoveCubeGripper(CubeGripper.Position.OPEN_ARMS));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}
