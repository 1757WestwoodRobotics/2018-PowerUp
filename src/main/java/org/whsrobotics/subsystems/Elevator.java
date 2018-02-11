package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class Elevator extends Subsystem {

    // Encoder rotates 4096 units per rotation

    private static TalonSRX left;
    private static TalonSRX right;

    // TODO: Tune!
    private static final double KP = 0.0;
    private static final double KI = 0.0;
    private static final double KD = 0.0;

    private static final int MAX_ERROR = 10;

    private static Elevator instance;

    enum Position {
        DOWN(0), MIDDLE(100), UP(500);

        private double target;

        Position(double target) {
            this.target = target;
        }

        public double getTargetValue() {
            return target;
        }

    }

    private Elevator() {

        try {
            left = new TalonSRX(RobotMap.MotorControllerPort.ELEVATOR_LEFT.getPort());
            right = new TalonSRX(RobotMap.MotorControllerPort.ELEVATOR_RIGHT.getPort());

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
            // left.setSensorPhase(true); // Used to invert the direction of the sensor

            left.configPeakOutputForward(2, 0);
            left.configPeakOutputReverse(-2, 0);

            // Native units TODO: tune
            left.configReverseSoftLimitThreshold(0,0);
            left.configForwardSoftLimitThreshold(1000,0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            left.configAllowableClosedloopError(0, MAX_ERROR, 0);

            left.config_kP(0, KP, 0);
            left.config_kI(0, KI, 0);
            left.config_kD(0, KD, 0);

            right.follow(left);
            right.setInverted(true);

        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Error setting up / configuring Elevator hardware!" + e.getMessage());
        }

    }

    public static Elevator getInstance() {
        if (instance == null) {
            instance = new Elevator();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static void moveToPosition(Position position) {
        left.set(ControlMode.Position, position.getTargetValue());
    }

    public static int getEncoderPosition() {
        return left.getSelectedSensorPosition(0);
    }

    public static int getEncoderVelocity() {
        return left.getSelectedSensorVelocity(0);
    }


}
