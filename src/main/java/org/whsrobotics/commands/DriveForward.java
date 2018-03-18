package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.Units;

public class DriveForward extends Command {

    private double meters = 0.0;


    public DriveForward(double distance, Units unit) {
        requires(DriveTrain.getInstance());

        this.meters = Units.convert(unit, Units.METERS, distance);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean isFinished() {

    }

}
