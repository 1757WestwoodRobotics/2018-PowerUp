package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.TimedCommand;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.utils.RobotLogger;

/**
 * Tightly grips the power cube in the arms
 */
public class CubeGripperApplyConstantVoltage extends InstantCommand {

    public CubeGripperApplyConstantVoltage() {
        requires(CubeGripper.getInstance());
    }

    @Override
    protected void execute() {
        CubeGripper.applyConstantVoltage();
    }

}
