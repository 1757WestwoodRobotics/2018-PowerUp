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
        INWARD(1), OUTWARD(-1), HALF_INWARD(0.5), HALF_OUTWARD(-0.5), OFF(0);

        private double speed;

        Mode(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private static CubeSpinner instance;

    private CubeSpinner() {

        try {
            left = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_LEFT.getPort());
            right = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_RIGHT.getPort());

            left.setNeutralMode(NeutralMode.Coast);
            right.setNeutralMode(NeutralMode.Coast);

            left.configPeakOutputForward(.50, 0);
            left.configPeakOutputReverse(-.50, 0);

            right.configPeakOutputForward(.50, 0);
            right.configPeakOutputReverse(-.50, 0);

            left.setInverted(true);
            right.follow(left);

        } catch (Exception e) {
            RobotLogger.err(this.getClass(), "Error instantiating CubeSpinner hardware" + e.getMessage());

        }

    }

    public static CubeSpinner getInstance() {
        if (instance == null) {
            instance = new CubeSpinner();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        // setDefaultCommand(new SpinCubeSpinner(Mode.OFF));
    }

    public static void spinWithSpeed(double speed){
        System.out.println(speed);
        left.set(ControlMode.PercentOutput, speed);

    }

    public static void spinWithMode(Mode mode){
        spinWithSpeed(mode.getSpeed());
    }

}