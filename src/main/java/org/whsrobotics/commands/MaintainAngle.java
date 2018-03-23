package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.RobotLogger;

public class MaintainAngle extends Command {

    // 0 = Forwards
    // 90 = Right
    // -90 = Left
    // -180/180 = Behind

    private double angle = 0.0;

    public MaintainAngle(double angle) {
        requires(DriveTrain.getInstance());
        this.angle = angle;
    }

    @Override
    protected void initialize() {
        try {
            DriveTrain.initializeRotationPIDController();
            DriveTrain.enableRotationPIDController();
        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error with RotationPID Initialization!", false);
            end();
        }
    }

    @Override
    protected void execute() {
        DriveTrain.turnToAngle(0.5, angle);
    }

    @Override
    protected void end() {
        DriveTrain.disableRotationPIDController();
        DriveTrain.stopDrive();
    }

    @Override
    protected boolean isFinished() {
//        if (DriveTrain.isRotationPIDControllerOnTarget()) {
//            RobotLogger.getInstance().log(this.getClass(), "TurnToAngle has reached the target angle.");
//            return true;
//        }

        return false;
    }
}
