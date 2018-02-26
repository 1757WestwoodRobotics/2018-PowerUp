package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.triggers.LimitSwitch;
import org.whsrobotics.utils.RobotLogger;

/**
 * Subsystem for running the robot elevator.
 *
 * @author Larry Tseng
 */
public class Elevator extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    private static LimitSwitch topLimit;
    private static LimitSwitch bottomLimit;

    // TODO: Retune! and zero out sensor at bottom, put PID setting code at bottom [commented out]
    private static double KP = 0.8;
    private static double KI = 0.0;
    private static double KD = 0.0;
    private static double KF = 0.0;

    private static final int MAX_ERROR = 20;

    private static Elevator instance;

    public enum Position {
        DOWN(0), SWITCH(10000), SCALE_TOP(23000);

        private double target;

        Position(double target) {
            this.target = target;
        }

        public double getTarget() {
            return target;
        }

    }

    private Elevator() {

        try {

            // ------------ HARDWARE ------------- //

            left = new TalonSRX(RobotMap.MotorControllerPort.ELEVATOR_LEFT.port);
            right = new TalonSRX(RobotMap.MotorControllerPort.ELEVATOR_RIGHT.port);

            left.setNeutralMode(NeutralMode.Brake);
            right.setNeutralMode(NeutralMode.Brake);

            left.configPeakOutputForward(.50, 0);   // TODO: Raise?
            left.configPeakOutputReverse(-.50, 0);

//            left.setSensorPhase(true);

            right.follow(left);
            left.setInverted(true);

            // ------------ PID ------------- //

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

            left.configReverseSoftLimitThreshold(0,0);   // Native units
            left.configForwardSoftLimitThreshold(23000,0);

            left.configReverseSoftLimitEnable(true, 0);
            left.configForwardSoftLimitEnable(true, 0);

            left.configAllowableClosedloopError(0, MAX_ERROR, 0);

            left.config_kP(0, KP, 0);
            left.config_kI(0, KI, 0);
            left.config_kD(0, KD, 0);
            left.config_kF(0, KF, 0);
            left.selectProfileSlot(0, 0);

            // Motion Magic //TODO: Retune
            left.configMotionCruiseVelocity(1000, 0);
            left.configMotionAcceleration(500, 0);

            // ------------ LIMIT SWITCH ------------- //

            topLimit = new LimitSwitch(RobotMap.DigitalInputPort.ELEVATOR_TOP.port);
            bottomLimit = new LimitSwitch(RobotMap.DigitalInputPort.ELEVATOR_BOTTOM.port);

        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Error setting up / configuring Elevator hardware!" + e.getMessage());
        }

        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);

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
            SmartDashboard.putBoolean("ElevatorTopLimit", Elevator.getTopLimitSwitch());
            SmartDashboard.putBoolean("ElevatorBottomLimit", Elevator.getBottomLimitSwitch());
        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Can't get Elevator encoder data!" + e.getMessage());
        }
    }

    /**
     * Testing code to easily set PID constants
     */
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

    /**
     * Moves the elevator to a specified elevator position using PID and MotionMagic.
     *
     * @see Elevator.Position
     * @param position The target elevator position
     */
    public static void moveToPosition(Position position) {
        setPID();   // TEMP
        System.out.println(position.getTarget());
        left.set(ControlMode.MotionMagic, position.getTarget());       // ControlMode.MotionMagic or ControlMode.Position
    }

    /**
     * Moves the elevator to a specified elevator absolute encoder tick (4096 ticks per revolution).
     *
     * @param target The target elevator position defined in absolute encoder ticks
     */
    public static void moveToDS(int target) {
        setPID();   // TEMP
        System.out.println(target); // TEMP
        left.set(ControlMode.Position, target); // TODO: Switch to MotionMagic?
    }

    /**
     * Moves the elevator up (positive) or down (negative) with PercentOutput
     *
     * @param speed The speed at which the elevator moves [-1, 1]
     */
    public static void moveWithVelocity(double speed) {
        left.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Gets the absolute position of the left talon's encoder
     *
     * @return The absolute position of the encoder
     */
    public static int getEncoderPosition() {
        return left.getSelectedSensorPosition(0);
    }

    /**
     * Gets the calculated velocity of the left talon's encoder
     *
     * @return The calculated velocity of the encoder
     */
    public static int getEncoderVelocity() {
        return left.getSelectedSensorVelocity(0);
    }

    private static void resetEncoderPosition() {
        left.setSelectedSensorPosition(0, 0, 0);
    }

    // ------------ FINISHED METHODS ------------- //

    public static boolean getPIDFinished() {
        return left.getClosedLoopError(0) <= MAX_ERROR;
    }

    public static boolean reachedBoundaries() {
        return getTopLimitSwitch() || getBottomLimitSwitch();
    }

    public static boolean getTopLimitSwitch() {
        return topLimit.get();
    }

    public static boolean getBottomLimitSwitch() {
        return bottomLimit.get();
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
}
