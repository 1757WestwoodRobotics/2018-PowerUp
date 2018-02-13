package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class Elevator extends Subsystem {

    // Encoder rotates 4096 units per rotation

    private static TalonSRX left;
    private static TalonSRX right;

    // TODO: Tune!
    private static double KP = 0.0;
    private static double KI = 0.0;
    private static double KD = 0.0;

    private static final int MAX_ERROR = 50;

    private static Elevator instance;

    public enum Position {
        DOWN(0), MIDDLE(100), UP(500);  // TODO: Reflect encoders

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

            left.configPeakOutputForward(0.5, 0);
            left.configPeakOutputReverse(-0.5, 0);

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

        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);

        SmartDashboard.putNumber("Elevator Target Position", 0);

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

    @Override
    public void periodic() {
        try {
            SmartDashboard.putNumber("EncPos", Elevator.getEncoderPosition());
            SmartDashboard.putNumber("EncVel", Elevator.getEncoderVelocity());
        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Can't get Elevator encoder data!" + e.getMessage());
        }
    }

    public static void setPID() {
        KP = SmartDashboard.getNumber("KP", KP);
        KI = SmartDashboard.getNumber("KI", KI);
        KD = SmartDashboard.getNumber("KD", KD);

        left.config_kP(0, KP, 0);
        left.config_kI(0, KI, 0);
        left.config_kD(0, KD, 0);

        System.out.println("KP: " + KP);
        System.out.println("KI: " + KI);
        System.out.println("KD: " + KD);
    }

    public static void moveToPosition(Position position) {
        setPID();   // TEMP
        left.set(ControlMode.Position, position.getTargetValue());
    }

    public static void moveToDS(int target) {
        setPID();   // TEMP
        left.set(ControlMode.Position, target);
    }

    public static void moveWithVelocity(double speed) {
        left.set(ControlMode.Velocity, speed);
    }

    public static int getEncoderPosition() {
        return left.getSelectedSensorPosition(0);
    }

    public static int getEncoderVelocity() {
        return left.getSelectedSensorVelocity(0);
    }

}
