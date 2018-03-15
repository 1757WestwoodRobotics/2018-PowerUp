package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

/**
 * Subsystem for running the robot elevator.
 *
 * @author Larry Tseng
 */
public class Elevator extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

//    private static LimitSwitch topLimit;
//    private static LimitSwitch bottomLimit;

    private static double KP = 0.8;
    private static double KI = 0.0;
    private static double KD = 0.0;
    private static double KF = 0.0;

    private static final int MAX_ERROR = 50;

    private static Elevator instance;

    public enum Position {
        DOWN(0), RAISE_UP(1000), SWITCH(10000), SCALE_TOP(26000);

        private int target;

        Position(int target) {
            this.target = target;
        }

        public int getTarget() {
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

            setNormalVoltageLimits();
            //
            //          Normal      Lift
            // FWD:     0.8         0.1
            // REV:     -0.2        -0.8
            //

            right.follow(left);
            left.setInverted(true);

            // ------------ PID ------------- //

            left.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

            left.configReverseSoftLimitThreshold(0,0);   // Native units
            left.configForwardSoftLimitThreshold(26000,0);  // TODO: Retune with real robot

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
            left.configMotionAcceleration(800, 0);

            resetEncoderPosition();

            // ------------ LIMIT SWITCH ------------- //

//            topLimit = new LimitSwitch(RobotMap.DigitalInputPort.ELEVATOR_TOP.port);
//            bottomLimit = new LimitSwitch(RobotMap.DigitalInputPort.ELEVATOR_BOTTOM.port);

        } catch (Exception e) {
            RobotLogger.getInstance().err(instance.getClass(), "Error setting up / configuring Elevator hardware!" + e.getMessage(), true);
        }

//        SmartDashboard.putNumber("KP", KP);
//        SmartDashboard.putNumber("KI", KI);
//        SmartDashboard.putNumber("KD", KD);
//        SmartDashboard.putNumber("KF", KF);

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
            RobotLogger.getInstance().err(instance.getClass(), "Can't get Elevator encoder data!" + e.getMessage(), false);
        }
    }

//    /**
//     * Testing code to easily set PID constants
//     */
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
//
//        System.out.println("KP: " + KP);
//        System.out.println("KI: " + KI);
//        System.out.println("KD: " + KD);
//        System.out.println("KF: " + KF);
//    }

    /**
     * Moves the elevator to a specified elevator absolute encoder tick (4096 ticks per revolution)
     * using PID and MotionMagic.
     *
     * @param target The target elevator position defined in absolute encoder ticks
     */
    public static void moveToValue(int target) {
        RobotLogger.getInstance().log(instance.getClass(), "Setting Elevator to: " + target);
        left.set(ControlMode.MotionMagic, target);
    }

    /**
     * Moves the elevator to a specified elevator position.
     *
     * @see Elevator.Position
     * @see #moveToValue(int)
     * @param position The target elevator position
     */
    public static void moveToPosition(Position position) {
        moveToValue(position.target);
    }

    /**
     * Moves the elevator up (positive) or down (negative) with PercentOutput
     *
     * @param speed The speed at which the elevator moves [-1, 1]
     */
    public static void moveWithVelocity(double speed) {
        left.set(ControlMode.PercentOutput, speed);
    }

    // TODO: Bind to button as an emergency?
    public static void setTalonNeutral() {
        left.neutralOutput();
        right.neutralOutput();
    }

    // CONFIG STUFF //

    public static void setNormalVoltageLimits() {
        left.configPeakOutputForward((2.0/3.0), 0);
        left.configPeakOutputReverse(-(1.0/3.0), 0);
    }

    public static void setEndgameVoltageLimits() {  // TODO: FIX LATER
        left.configPeakOutputForward((2.0/3.0), 0);
        left.configPeakOutputReverse(-(1.0/3.0), 0);
    }

    // ENCODER STUFF //

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
//
//    public static boolean reachedBoundaries() {
//        return getTopLimitSwitch() || getBottomLimitSwitch();
//    }
//
//    public static boolean getTopLimitSwitch() {
//        return topLimit.get();
//    }
//
//    public static boolean getBottomLimitSwitch() {
//        return bottomLimit.get();
//    }

    public static double getError() {
        return left.getClosedLoopError(0);
    }

    public static boolean reachedTargetRange(int value) {
        double max = value + 600;
        double min = value - 600;
        double current = Elevator.getEncoderPosition();

        if (current >= min && current <= max) {
            System.out.println("Elevator has reached target!");
            return true;
        }

        return false;

        // return getError() < MAX_ERROR;
    }

    public static boolean reachedTargetRange(Position position) {
        return reachedTargetRange(position.getTarget());
    }

    public static int getMaxError() {
        return MAX_ERROR;
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
