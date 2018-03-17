package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeGripper extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Position {
        FOLD_BACK(0), ALMOST_FOLD(320), OPEN_ARMS(1550), GRAB_CUBE(2048);  // Native Encoder Units // 1 complete rotation = 4096 ticks

        // FOLD_BACK is folded back
        // OPEN_ARMS is at a ~45º angle, easier to drive up to a cube
        // GRAB_CUBE is where the arms are parallel [NOT NEEDED]

        private int target;

        Position(int target) {
            this.target = target;
        }

        public int getTarget() {
            return target;
        }
    }

    private static double LEFT_KP = 3.2;
    private static double RIGHT_KP = 3.2;
    private static double KI = 0.0;
    private static double KD = 0.0;
    private static double KF = 0.0;

    private static final int MAX_ERROR = 5;

    private static CubeGripper instance;

    private CubeGripper() {
        try {

            // ------------ HARDWARE ------------- //

            left = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_LEFT.port);
            right = new TalonSRX(RobotMap.MotorControllerPort.GRIPPER_RIGHT.port);

            left.setNeutralMode(NeutralMode.Brake);
            right.setNeutralMode(NeutralMode.Brake);

            left.configPeakOutputForward(.50, 0);
            left.configPeakOutputReverse(-.50, 0);

            right.configPeakOutputForward(.50, 0);
            right.configPeakOutputReverse(-.50, 0);

            right.setInverted(true);

            left.setSensorPhase(true);
            right.setSensorPhase(true);

            // ------------ PID ------------- //

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
            right.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

            left.configReverseSoftLimitThreshold(0,0);   // Native units
            left.configForwardSoftLimitThreshold(2200,0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            right.configReverseSoftLimitThreshold(0,0);   // Native units
            right.configForwardSoftLimitThreshold(2200,0);

            right.configReverseSoftLimitEnable(true, 0);
            right.configForwardSoftLimitEnable(true, 0);

            left.configAllowableClosedloopError(0, MAX_ERROR, 0);
            right.configAllowableClosedloopError(0, MAX_ERROR, 0);

            left.config_kP(0, LEFT_KP, 0);
            left.config_kI(0, KI, 0);
            left.config_kD(0, KD, 0);
            left.config_kF(0, KF, 0);
            left.selectProfileSlot(0, 0);

            right.config_kP(0, RIGHT_KP, 0);
            right.config_kI(0, KI, 0);
            right.config_kD(0, KD, 0);
            right.config_kF(0, KF, 0);
            right.selectProfileSlot(0, 0);

            // Motion Magic
            left.configMotionCruiseVelocity(200, 0);
            left.configMotionAcceleration(200, 0);

            right.configMotionCruiseVelocity(200, 0);
            right.configMotionAcceleration(200, 0);

        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error instantiating CubeGripper hardware" + e.getMessage(), true);

        }
//
//        SmartDashboard.putNumber("KP", KP);
//        SmartDashboard.putNumber("KI", KI);
//        SmartDashboard.putNumber("KD", KD);
//        SmartDashboard.putNumber("KF", KF);
//
//        SmartDashboard.putNumber("CubeGripper Target Position", 0);

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
        } catch (Exception e) {
            RobotLogger.getInstance().err(instance.getClass(), "Can't get CubeGripper encoder data!" + e.getMessage(), false);
        }
    }

//    public static void setPID() {
//        KP = SmartDashboard.getNumber("KP", KP);
//        KI = SmartDashboard.getNumber("KI", KI);
//        KD = SmartDashboard.getNumber("KD", KD);
//        KF = SmartDashboard.getNumber("KD", KF);
//
//        left.config_kP(0, KP, 0);
//        left.config_kI(0, KI, 0);
//        left.config_kD(0, KD, 0);
//        left.config_kF(0, KF, 0);
//
//        right.config_kP(0, KP, 0);
//        right.config_kI(0, KI, 0);
//        right.config_kD(0, KD, 0);
//        right.config_kF(0, KF, 0);
//
//        RobotLogger.getInstance().log(instance.getClass(), "KP: " + KP);
//        RobotLogger.getInstance().log(instance.getClass(), "KI: " + KI);
//        RobotLogger.getInstance().log(instance.getClass(), "KD: " + KD);
//        RobotLogger.getInstance().log(instance.getClass(), "KF: " + KF);
//    }

    public static void moveToDS(int target) {
        System.out.println(target); // TEMP

        left.set(ControlMode.MotionMagic, target);
        right.set(ControlMode.MotionMagic, target);
    }

    public static void moveToPosition(Position position) {
        moveToDS(position.getTarget());
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

    // ------------ AUXILIARY COMMANDS ------------- //

    public static Command resetEncoderPositionCommand = new Command() {

        @Override
        protected void execute() {
            resetEncoderPosition();
        }

        @Override
        protected boolean isFinished() {
            return true;
        }
    };

    public static Command disableOutputCommand = new Command() {

        @Override
        protected void execute() {
             setTalonNeutral();
        }

        @Override
        protected boolean isFinished() {
            return true;
        }

    };

}