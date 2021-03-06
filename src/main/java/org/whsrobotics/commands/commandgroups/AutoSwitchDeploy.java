package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.*;
import org.whsrobotics.robot.Autonomous;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.Elevator;

public class AutoSwitchDeploy extends CommandGroup {

    public AutoSwitchDeploy(Autonomous.StartingPosition startingPosition, Autonomous.Ownership switchSide) {

        boolean sameSide =
                ((startingPosition == Autonomous.StartingPosition.LEFT) && (switchSide == Autonomous.Ownership.LEFT)) ||
                ((startingPosition == Autonomous.StartingPosition.RIGHT) && (switchSide == Autonomous.Ownership.RIGHT));

        if (sameSide) {

            // Force Grip
            // Move Forwards
            // Bring up Elevator
            // Spin outwards
            // Fold Arms
            // Bring down Elevator

            addSequential(new CubeGripperApplyConstantVoltage());
            addSequential(new DriveTimed(2));
            addSequential(new MoveElevatorPosition(Elevator.Position.SWITCH));
            addSequential(new TimedCommand(2));

            addSequential(new DriveTimed(1));

            addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));
            addSequential(new TimedCommand(1));
            addSequential(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

            addSequential(new MoveCubeGripper(CubeGripper.Position.ALMOST_FOLD));
            addSequential(new MoveElevatorPosition(Elevator.Position.DOWN));
            addSequential(new TimedCommand(2));

        } else {

            // Just Drive Forwards

            addSequential(new DriveTimed(2));

        }

    }

}
