package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.CubeSpinner;

public class SpinCubeSpinner extends InstantCommand {

    public SpinCubeSpinner() {
        requires(CubeSpinner.getInstance());
    }

    @Override
    protected void execute() {
        CubeSpinner.spinWithMode(OI.getSelectedCubeSpinnerMode());
    }

}
