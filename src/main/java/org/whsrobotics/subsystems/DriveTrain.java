package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.whsrobotics.commands.DefaultDrive;
import org.whsrobotics.robot.OI;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;
import org.whsrobotics.utils.Units;

import java.io.File;

public class DriveTrain extends Subsystem {

    private static WPI_TalonSRX leftFront;
    private static WPI_TalonSRX leftBack;
    private static WPI_TalonSRX rightFront;
    private static WPI_TalonSRX rightBack;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static AHRS navX;
    private static Encoder leftEncoder;
    private static Encoder rightEncoder;

    private static PIDController rotationPIDController;
    private static PIDController leftPositionPIDController;
    private static PIDController rightPositionPIDController;

    private static final double KP = 0.05;
    private static final double KI = 0;
    private static final double KD = 0.125;

    private static final double wheelDiameterInches = 4.3;
    private static final double wheelDiameterInMM = wheelDiameterInches * 25.4;
    private static final double wheelDiameterInMeters = wheelDiameterInMM / 1000.0;
    private static final double wheelCircumference = wheelDiameterInMeters * Math.PI;
    private static final double encoderResolution = 2048;
    private static final double distancePerPulse = wheelCircumference / encoderResolution;

    private static double rotationPIDOutput;

    private static final double ROT_TOLERANCE_DEG = 0.5f;
    private static final double POS_TOLERANCE_TICKS = 20;

    private static DriveTrain instance;

    private DriveTrain() {

        try {
            leftFront = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_LEFT_FRONT.port);
            leftBack = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_LEFT_BACK.port);
            rightFront = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_RIGHT_FRONT.port);
            rightBack = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_RIGHT_BACK.port);

            setCoastMode();

            leftFront.configPeakOutputForward(1, 0);
            leftBack.configPeakOutputForward(1, 0);
            rightFront.configPeakOutputForward(1, 0);
            rightBack.configPeakOutputForward(1, 0);

            leftFront.configPeakOutputReverse(-1, 0);
            leftBack.configPeakOutputReverse(-1, 0);
            rightFront.configPeakOutputReverse(-1, 0);
            rightBack.configPeakOutputReverse(-1, 0);

            // ONLY if needed
            setDriveTrainAccelLimit();

            leftDrive = new SpeedControllerGroup(leftFront, leftBack);
            rightDrive = new SpeedControllerGroup(rightFront, rightBack);

            differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

            navX = new AHRS(RobotMap.NAVX_PORT);
            resetNavXYaw();

            leftEncoder = new Encoder(RobotMap.DigitalInputPort.ENCODER_LEFT_A.port,
                    RobotMap.DigitalInputPort.ENCODER_LEFT_B.port);
            leftEncoder.setDistancePerPulse(distancePerPulse);
            rightEncoder = new Encoder(RobotMap.DigitalInputPort.ENCODER_RIGHT_A.port,
                    RobotMap.DigitalInputPort.ENCODER_RIGHT_B.port, true);
            rightEncoder.setDistancePerPulse(distancePerPulse);
            resetEncoders();

        } catch (NullPointerException e) {
            RobotLogger.getInstance().err(this.getClass(), "Error instantiating the DriveTrain hardware!" + e.getMessage(), true);

        }

    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DefaultDrive());
    }

    @Override
    public void periodic() {
        try {
            SmartDashboard.putNumber("NavX - Yaw", getYawAngle());
        } catch (Exception e) {
            RobotLogger.getInstance().err(instance.getClass(), "Error reading NavX data!" + e.getMessage(), true);
        }
    }

    // ------------ DRIVETRAIN METHODS ------------- //

    private static void drive(double speed, double rotation, boolean squaredInputs) {
        differentialDrive.arcadeDrive(speed, rotation, squaredInputs);
    }

    /**
     * Arcade drive with input curving, and xbox controller deadzone implementation
     */
    public static void controllerDrive(double speed, double rotation) {
        drive(OI.leftXboxJoystickCurve(speed), OI.rightXboxJoystickCurve(rotation), false);
    }

    public static void setDriveTrainAccelLimit() {
        leftFront.configOpenloopRamp(0.5, 0);
        leftBack.configOpenloopRamp(0.5, 0);
        rightFront.configOpenloopRamp(0.5, 0);
        rightBack.configOpenloopRamp(0.5, 0);
    }

    public static void removeDriveTrainAccelLimit() {
        leftFront.configOpenloopRamp(0, 0);
        leftBack.configOpenloopRamp(0, 0);
        rightFront.configOpenloopRamp(0, 0);
        rightBack.configOpenloopRamp(0, 0);
    }

    public static void setBrakeMode() {
        leftFront.setNeutralMode(NeutralMode.Brake);
        leftBack.setNeutralMode(NeutralMode.Brake);
        rightFront.setNeutralMode(NeutralMode.Brake);
        rightBack.setNeutralMode(NeutralMode.Brake);
    }

    public static void setCoastMode() {
        leftFront.setNeutralMode(NeutralMode.Coast);
        leftBack.setNeutralMode(NeutralMode.Coast);
        rightFront.setNeutralMode(NeutralMode.Coast);
        rightBack.setNeutralMode(NeutralMode.Coast);
    }

    public static void stopDrive() {
        differentialDrive.stopMotor();
    }

    // ------------ NAVX METHODS ------------- //

    public static void resetNavXYaw() {
        navX.reset();
    }

    public static double getYawAngle() {
        return navX.getYaw();
    }

    // ------------ ENCODER METHODS ------------- //

    private static void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public static int getLeftEncoderCount() {
        return leftEncoder.get();
    }

    public static int getRightEncoderCount() {
        return rightEncoder.get();
    }

    public static double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }

    public static double getRightEncoderDistance() {
        return rightEncoder.getDistance();
    }

    public static double getLeftEncoderRate() {
        return leftEncoder.getRate();
    }

    public static double getRightEncoderRate() {
        return rightEncoder.getRate();
    }

    public static void initializeLeftPositionPIDController() {
        if (leftPositionPIDController == null) {
            try {
                leftPositionPIDController = new PIDController(0.5, KI, KD, leftEncoder, leftDrive);

                leftPositionPIDController.setAbsoluteTolerance(POS_TOLERANCE_TICKS);
                leftPositionPIDController.setOutputRange(-1.0, 1.0);
                leftPositionPIDController.disable();
            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error creating the DriveTrain LeftPosition PIDController!" + e.getMessage(), true);
            }
        }
    }

    public static void initializeRightPositionPIDController() {

        if (rightPositionPIDController == null) {
            try {
                rightPositionPIDController = new PIDController(0.5, KI, KD, rightEncoder, rightDrive);

                rightPositionPIDController.setAbsoluteTolerance(POS_TOLERANCE_TICKS);
                rightPositionPIDController.setOutputRange(-1.0, 1.0);
                rightPositionPIDController.disable();
            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error creating the DriveTrain RightPosition PIDController!" + e.getMessage(), true);
            }
        }
    }

    public static void enablePositionPIDControllers() {
        leftPositionPIDController.enable();
    }

    public static void disablePositionPIDControllers() {
        rightPositionPIDController.enable();
    }

    public static double getLeftPositionPIDOutput() {
        return leftPositionPIDController.get();
    }

    public static double getRightPositionPIDOutput() {
        return rightPositionPIDController.get();
    }

    /**
     * In meters
     */
    public static void setPositionPID(double meters) {
        System.out.println("setting left setpoint");
        leftPositionPIDController.setSetpoint(meters);
        System.out.println("setting right setpoint");
        rightPositionPIDController.setSetpoint(meters);
    }

    public static void setPositionPIDInMeters(double meters) {
        setPositionPID(meters);
    }

    public static boolean arePositionPIDControllersOnTarget() {
        return leftPositionPIDController.onTarget() && rightPositionPIDController.onTarget();
    }


    // ------------ ANGLE TURNING / ROTATION PID METHODS ------------- //

    public static void initializeRotationPIDController() {

        if (rotationPIDController == null) {

            try {
                rotationPIDController = new PIDController(KP, KI, KD, navX, (output) -> rotationPIDOutput = -output);

                rotationPIDController.setAbsoluteTolerance(ROT_TOLERANCE_DEG);
                rotationPIDController.setInputRange(-180.0, 180.0);
                rotationPIDController.setOutputRange(-1.0, 1.0);
                rotationPIDController.setContinuous(true);
                rotationPIDController.disable();

            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error creating the DriveTrain Rotation PIDController!" + e.getMessage(), true);
            }

        }

    }

    public static void enableRotationPIDController() {
        RobotLogger.getInstance().log(instance.getClass(), "Enabling rotationPIDController");
        rotationPIDController.enable();
    }

    public static void disableRotationPIDController() {
        RobotLogger.getInstance().log(instance.getClass(), "Disabling rotationPIDController");
        rotationPIDController.disable();
    }

    public static void turnToAngle(double speed, double angle) {
        rotationPIDController.setSetpoint(angle);
        drive(speed, rotationPIDOutput/1.5, false);
    }

    public static boolean isRotationPIDControllerOnTarget() {
        return rotationPIDController.onTarget();
    }

    public static double getRotationPIDControllerSetpoint() {
        return rotationPIDController.getSetpoint();
    }

    // ------------ PATHFINDER CONFIG / METHODS ------------- //

    private static Trajectory.Config config;
    private static final Trajectory.FitMethod fitMethod = Trajectory.FitMethod.HERMITE_CUBIC;
    private static final int samples = Trajectory.Config.SAMPLES_HIGH;
    private static final double timeStep = 0.05;         // time delta between points
    private static final double maxVelocity = 1.7;       // m/s // TODO: MEASURE & CALCULATE
    private static final double maxAcceleration = 2.0;   // m/s/s
    private static final double maxJerk = 60.0;          // m/s/s/s

    private static final double wheelbaseWidth = 0.442;   // TODO: MEASURE (width of robot from left wheel to right wheel) (meters)

    private static void configPathfinder() {
        config = new Trajectory.Config(fitMethod, samples, timeStep, maxVelocity, maxAcceleration, maxJerk);
    }

    public static Trajectory generateTrajectory(Waypoint[] points) {
        return Pathfinder.generate(points, config);
    }

    public static Trajectory generateTrajectoryFromFile(File file) {
        return Pathfinder.readFromCSV(file);
    }

    public static void applyTrajectories(Trajectory left, Trajectory right) {
        // TODO: Implement!
    }

    public static void applyTrajectory(Trajectory trajectory) {
        TankModifier tankModifier = new TankModifier(trajectory).modify(wheelbaseWidth);

        Trajectory leftTrajectory = tankModifier.getLeftTrajectory();
        Trajectory rightTrajectory = tankModifier.getRightTrajectory();

        EncoderFollower leftEnc = new EncoderFollower(leftTrajectory);
        EncoderFollower rightEnc = new EncoderFollower(rightTrajectory);

        leftEnc.configureEncoder(leftEncoder.get(), 2048, wheelbaseWidth);
        rightEnc.configureEncoder(rightEncoder.get(), 2048, wheelbaseWidth);

        leftEnc.configurePIDVA(1.0, 0, 0, 1 / maxVelocity, 0);
        rightEnc.configurePIDVA(1.0, 0, 0, 1 / maxVelocity, 0);

        // TODO: encoder.get() or .getDistance???

        double leftOutput = leftEnc.calculate(leftEncoder.get());   // Output directly to drive (or PID Controller?)
        double rightOutput = rightEnc.calculate(rightEncoder.get());    // return array to command? or call drive()

        // TODO: turn? or use TurnToAngle

        leftDrive.set(leftOutput);
        rightDrive.set(rightOutput);

    }

}

