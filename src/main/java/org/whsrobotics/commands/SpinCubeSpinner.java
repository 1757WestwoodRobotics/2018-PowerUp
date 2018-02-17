package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.subsystems.CubeSpinner;

public class SpinCubeSpinner extends InstantCommand {

    private CubeSpinner.Mode mode;

    public SpinCubeSpinner(CubeSpinner.Mode mode) {
        requires(new CubeSpinner());    // TODO: Convert to singleton getInstance()
        this.mode = mode;
    }

    @Override
    protected void execute() {
        CubeSpinner.spinWithMode(mode);
    }

}
