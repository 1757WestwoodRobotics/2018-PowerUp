package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeGripper extends Subsystem {


    private static TalonSRX left;
    private static TalonSRX right;

    public CubeGripper(){

        try {
            left = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_LEFT.getPort());
            right = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_RIGHT.getPort());

        } catch (Exception e) {
            RobotLogger.err(this.getClass(), "Error instantiating CubeGripper hardware" + e.getMessage());

        }
    }

    @Override
    protected void initDefaultCommand() {

    }
}