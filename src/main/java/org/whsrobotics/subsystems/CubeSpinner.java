package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeSpinner extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Mode {
        FORWARD(1), BACKWARD(-1), HALF_FORWARD(0.5), HALF_BACKWARD(-0.5);

        private double speed;

        Mode(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    public CubeSpinner(){

        try {
            left = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_LEFT.getPort());
            right = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_RIGHT.getPort());

            right.setInverted(true);
            right.follow(left);

        } catch (Exception e) {
            RobotLogger.err(this.getClass(), "Error instantiating CubeSpinner hardware" + e.getMessage());

        }

    }

    @Override
    protected void initDefaultCommand() {

    }

    public static void spinWithSpeed(double speed) {
        left.set(ControlMode.PercentOutput, speed);

    }

    public static void spinWithMode(Mode mode) {
        spinWithSpeed(mode.getSpeed());
    }

}