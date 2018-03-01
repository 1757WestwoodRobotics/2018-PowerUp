package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.robot.OI;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeSpinner extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Mode {
        INWARDS(1), OUTWARDS(-1), OFF(0);

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
            left = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_LEFT.port);
            right = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_RIGHT.port);

            left.setNeutralMode(NeutralMode.Coast);
            right.setNeutralMode(NeutralMode.Coast);

            left.configPeakOutputForward(.50, 0);
            left.configPeakOutputReverse(-.50, 0);

            left.setInverted(true);
            right.follow(left);

        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error instantiating CubeSpinner hardware" + e.getMessage());

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

    @Override
    public void periodic() {
        // System.out.println(OI.getSelectedCubeSpinnerMode());
    }

    public static void spinWithSpeed(double speed) {
        System.out.println(speed);
        left.set(ControlMode.PercentOutput, speed);

    }

    public static void spinWithMode(Mode mode) {
        System.out.println(mode);
        spinWithSpeed(mode.getSpeed());
    }


    public static int getIRSensor() {
        return right.getSensorCollection().getAnalogIn();
    }
}