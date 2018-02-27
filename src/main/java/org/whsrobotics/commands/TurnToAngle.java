package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.RobotLogger;

public class TurnToAngle extends Command {

    private double angle = 0.0;

    public TurnToAngle(double angle) {
        requires(DriveTrain.getInstance());
        this.angle = angle;
    }

    @Override
    protected void initialize() {
        if (!DriveTrain.initializeRotationPIDController()) {
            RobotLogger.getInstance().err(this.getClass(), "Error initializing rotationPIDController. Cannot TurnToAngle! Ending command.");
            end();
        }

    }

    @Override
    protected void execute() {
        DriveTrain.turnToAngle(OI.getXboxController().getY(GenericHID.Hand.kLeft), angle);
    }

    @Override
    protected void end() {
        DriveTrain.stopDrive();
    }

    @Override
    protected boolean isFinished() {
        if (DriveTrain.isRotationPIDControllerOnTarget()) {
            RobotLogger.getInstance().log(this.getClass(), "TurnToAngle has reached the target angle.");
            return true;
        }

        return false;
    }
}
