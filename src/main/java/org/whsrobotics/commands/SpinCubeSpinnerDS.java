package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.CubeSpinner;

public class SpinCubeSpinnerDS extends InstantCommand {

    public SpinCubeSpinnerDS() {

    }

    @Override
    protected void execute() {
        (new SpinCubeSpinner(OI.getSelectedCubeSpinnerMode())).start();
    }
}
