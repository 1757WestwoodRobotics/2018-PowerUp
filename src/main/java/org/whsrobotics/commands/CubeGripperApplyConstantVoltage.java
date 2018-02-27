package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.CubeGripper;

/**
 * Tightly grips the power cube in the arms
 */
public class CubeGripperApplyConstantVoltage extends Command {

    public CubeGripperApplyConstantVoltage() {
        super(60);  // After 1 minute, the command will end due to motor heat
        requires(CubeGripper.getInstance());
    }

    @Override
    protected void initialize() {
        CubeGripper.applyConstantVoltage();
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        CubeGripper.setTalonNeutral();
    }

}
