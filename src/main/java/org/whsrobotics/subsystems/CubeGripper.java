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

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Position {
        STORE(0), OPEN(100), CLOSE(500);  // TODO: Reflect encoders

        private double target;

        Position(double target) {
            this.target = target;
        }

        public double getTarget() {
            return target;
        }
    }

    // TODO: Tune!
    private static double KP = 0.0;
    private static double KI = 0.0;
    private static double KD = 0.0;
    private static double KF = 0.0;

    private static final int MAX_ERROR = 50;

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
            right.setInverted(true);

            // ------------ PID ------------- //

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

            left.configReverseSoftLimitThreshold(4000,0);   // Native units
            left.configForwardSoftLimitThreshold(26000,0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            right.configReverseSoftLimitThreshold(4000,0);   // Native units
            right.configForwardSoftLimitThreshold(26000,0);

            right.configReverseSoftLimitEnable(true, 0);
            right.configForwardSoftLimitEnable(true, 0);

            left.configAllowableClosedloopError(0, MAX_ERROR, 0);
            left.configAllowableClosedloopError(0, MAX_ERROR, 0);

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
            SmartDashboard.putNumber("CubeGripPos", getEncoderPosition());
            SmartDashboard.putNumber("CubeGripVel", getEncoderVelocity());
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


        System.out.println("KP: " + KP);
        System.out.println("KI: " + KI);
        System.out.println("KD: " + KD);
        System.out.println("KF: " + KF);
    }

    public static void moveToDS(int target) {
        setPID();   // TEMP
        System.out.println(target); // TEMP
        left.set(ControlMode.Position, target);
    }

    public static int getEncoderPosition() {
        return left.getSelectedSensorPosition(0);
    }

    public static int getEncoderVelocity() {
        return left.getSelectedSensorVelocity(0);
    }

}