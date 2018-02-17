package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeSpinner extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Mode {
        FORWARD(1), BACKWARD(-1), HALF_FORWARD(0.5), HALF_BACKWARD(-0.5), OFF(0);

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

            left.setNeutralMode(NeutralMode.Coast);
            right.setNeutralMode(NeutralMode.Coast);

            left.configPeakOutputForward(.50, 0);
            left.configPeakOutputReverse(-.50, 0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            right.setInverted(true);
            right.follow(left);

        } catch (Exception e) {
            RobotLogger.err(this.getClass(), "Error instantiating CubeSpinner hardware" + e.getMessage());

        }

    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new SpinCubeSpinner(Mode.OFF));
    }

    public static void spinWithSpeed(double speed) {
        left.set(ControlMode.PercentOutput, speed);

    }

    public static void spinWithMode(Mode mode) {
        spinWithSpeed(mode.getSpeed());
    }

}