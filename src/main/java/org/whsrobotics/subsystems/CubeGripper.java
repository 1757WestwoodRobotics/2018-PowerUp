package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeGripper extends Subsystem {

    // TODO: Reset encoder with homing mode (limit switch detector)?

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Position {
        STORE(0), OPEN(100), CLOSE(500), START_MATCH(-90), RECEIVE_CUBE(90);  // TODO: Reflect encoders, DEGREES conversion

        private double target;

        Position(double target) {
            this.target = target;
        }

        public double getTarget() {
            return target;
        }
    }

    // TODO: Tune!
    private static double KP = 0.8;
    private static double KI = 0.0;
    private static double KD = 0.0;
    private static double KF = 0.0;

    private static final int MAX_ERROR = 5;

    private static CubeGripper instance;

    private CubeGripper() {
        try {

            // ------------ HARDWARE ------------- //

            left = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_LEFT.getPort());
            right = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_RIGHT.getPort());

            left.setNeutralMode(NeutralMode.Brake);
            right.setNeutralMode(NeutralMode.Brake);

            left.configPeakOutputForward(.50, 0);
            left.configPeakOutputReverse(-.50, 0);

            right.configPeakOutputForward(.50, 0);
            right.configPeakOutputReverse(-.50, 0);

            left.setInverted(true);
            // right.setInverted(true);

            // ------------ PID ------------- //

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
            right.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

            left.configReverseSoftLimitThreshold(-533,0);   // Native units
            left.configForwardSoftLimitThreshold(1200,0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            right.configReverseSoftLimitThreshold(-533,0);   // Native units
            right.configForwardSoftLimitThreshold(1200,0);

            right.configReverseSoftLimitEnable(true, 0);
            right.configForwardSoftLimitEnable(true, 0);

            left.configAllowableClosedloopError(0, MAX_ERROR, 0);
            right.configAllowableClosedloopError(0, MAX_ERROR, 0);

            left.config_kP(0, KP, 0);
            left.config_kI(0, KI, 0);
            left.config_kD(0, KD, 0);
            left.config_kF(0, KF, 0);
            left.selectProfileSlot(0, 0);

            right.config_kP(0, KP, 0);
            right.config_kI(0, KI, 0);
            right.config_kD(0, KD, 0);
            right.config_kF(0, KF, 0);
            right.selectProfileSlot(0, 0);

        } catch (Exception e) {
            RobotLogger.err(this.getClass(), "Error instantiating CubeGripper hardware" + e.getMessage());

        }

        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);

        SmartDashboard.putNumber("CubeGripper Target Position", 0);

    }

    public static CubeGripper getInstance() {
        if (instance == null) {
            instance = new CubeGripper();
        }
        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void periodic() {
        try {
            SmartDashboard.putNumber("CubeGripLeftPos", getLeftEncoderPosition());
            SmartDashboard.putNumber("CubeGripLeftVel", getLeftEncoderVelocity());
            SmartDashboard.putNumber("CubeGripRightPos", getRightEncoderPosition());
            SmartDashboard.putNumber("CubeGripRightVel", getRightEncoderVelocity());

            SmartDashboard.putNumber("CubeGripLeftPIDPosition", left.getClosedLoopTarget(0));
            SmartDashboard.putNumber("CubeGripRightPIDPosition", right.getClosedLoopTarget(0));
        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Can't get CubeGripper encoder data!" + e.getMessage());
        }
    }

    public static void setPID() {
        KP = SmartDashboard.getNumber("KP", KP);
        KI = SmartDashboard.getNumber("KI", KI);
        KD = SmartDashboard.getNumber("KD", KD);
        KF = SmartDashboard.getNumber("KD", KF);

        left.config_kP(0, KP, 0);
        left.config_kI(0, KI, 0);
        left.config_kD(0, KD, 0);
        left.config_kF(0, KF, 0);

        right.config_kP(0, KP, 0);
        right.config_kI(0, KI, 0);
        right.config_kD(0, KD, 0);
        right.config_kF(0, KF, 0);

        System.out.println("KP: " + KP);
        System.out.println("KI: " + KI);
        System.out.println("KD: " + KD);
        System.out.println("KF: " + KF);
    }

    public static void moveToDS(int target) {
        setPID();   // TEMP
        System.out.println(target); // TEMP
        left.set(ControlMode.Position, target);
        right.set(ControlMode.Position, target);
    }

    public static int getLeftEncoderPosition() {
        return left.getSelectedSensorPosition(0);
    }

    public static int getLeftEncoderVelocity() {
        return left.getSelectedSensorVelocity(0);
    }

    public static int getRightEncoderPosition() {
        return right.getSelectedSensorPosition(0);
    }

    public static int getRightEncoderVelocity() {
        return right.getSelectedSensorVelocity(0);
    }

    public static void setTalonNeutral() {
        left.neutralOutput();
        right.neutralOutput();
    }

    public static void resetEncoderPosition() {
        left.setSelectedSensorPosition(0, 0, 0);
        right.setSelectedSensorPosition(0, 0, 0);
    }

    public static void applyConstantVoltage() {
        left.set(ControlMode.PercentOutput, 1);
        right.set(ControlMode.PercentOutput, 1);
    }

}