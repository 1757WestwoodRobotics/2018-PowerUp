package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.utils.AutomationCancelerHelper;

public class CGDeployCubeToSwitch extends CommandGroup {

    private final Elevator.Position position = Elevator.Position.SWITCH;

    public CGDeployCubeToSwitch(){

        requires(new AutomationCancelerHelper());
//
//        addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));
//        addSequential(new Command() {
//            @Override
//            protected boolean isFinished() {
//                return Elevator.reachedTarget(Elevator.Position.SWITCH.getTarget());
//            }
//        });

        // TODO: TEST
        addSequential(new ExecuteCommandWithFinishable(new MoveElevatorPosition(position),
                () -> Elevator.reachedTarget(position)), 8);    // If it doesn't get there in 3 seconds, move on.

        // Spin the CubeSpinner motors in the OUTWARDS mode (until Cube has left the ultrasonic sensor), and open arms
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));

        addSequential(new TimedCommand(2));

        addSequential(new MoveCubeGripper(CubeGripper.Position.ALMOST_FOLD));
        addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));

    }

}
