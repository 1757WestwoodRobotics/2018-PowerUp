package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
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
    private static PIDController encoderPIDController;

    private static final double KP = 0.05;   // Tuned values for the test robot (Pneumatic wheels)
    private static final double KI = 0;
    private static final double KD = 0.125;

    private static double rotationPIDOutput;
    private static final double ROT_TOLERANCE_DEG = 0.5f;

    private static DriveTrain instance;

    private DriveTrain() {

        try {
            leftFront = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_LEFT_FRONT.port);
            leftBack = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_LEFT_BACK.port);
            rightFront = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_RIGHT_FRONT.port);
            rightBack = new WPI_TalonSRX(RobotMap.MotorControllerPort.DRIVE_RIGHT_BACK.port);

            leftFront.configPeakOutputForward(1, 0);
            leftBack.configPeakOutputForward(1, 0);
            rightFront.configPeakOutputForward(1, 0);
            rightBack.configPeakOutputForward(1, 0);

            leftFront.configPeakOutputReverse(-1, 0);
            leftBack.configPeakOutputReverse(-1, 0);
            rightFront.configPeakOutputReverse(-1, 0);
            rightBack.configPeakOutputReverse(-1, 0);

            leftDrive = new SpeedControllerGroup(leftFront, leftBack);
            rightDrive = new SpeedControllerGroup(rightFront, rightBack);

            differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

            navX = new AHRS(RobotMap.NAVX_PORT);
            resetNavXYaw();

            leftEncoder = new Encoder(RobotMap.DigitalInputPort.ENCODER_LEFT_A.port,
                    RobotMap.DigitalInputPort.ENCODER_LEFT_B.port,
                    RobotMap.DigitalInputPort.ENCODER_LEFT_INDEX.port);
            leftEncoder.setDistancePerPulse(0);  // Circumference (in meters)/2048 (resolution of the encoder)
            rightEncoder = new Encoder(RobotMap.DigitalInputPort.ENCODER_RIGHT_A.port,
                    RobotMap.DigitalInputPort.ENCODER_RIGHT_B.port,
                    RobotMap.DigitalInputPort.ENCODER_RIGHT_INDEX.port);
            rightEncoder.setDistancePerPulse(0);
            resetEncoders();

        } catch (NullPointerException e) {
            RobotLogger.err(this.getClass(), "Error instantiating the DriveTrain hardware!" + e.getMessage());

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
            SmartDashboard.putNumber("LF", leftFront.getMotorOutputVoltage());
            SmartDashboard.putNumber("LB", leftBack.getMotorOutputVoltage());
            SmartDashboard.putNumber("RF", rightFront.getMotorOutputVoltage());
            SmartDashboard.putNumber("RB", rightBack.getMotorOutputVoltage());
            SmartDashboard.putNumber("Xbox", OI.checkXboxDeadzone(OI.getXboxController().getX(GenericHID.Hand.kRight)));
        } catch (Exception e) {
            RobotLogger.err(instance.getClass(), "Error reading NavX data!" + e.getMessage());
        }
    }

    // ------------ DRIVETRAIN METHODS ------------- //

    private static void drive(double x, double y, boolean squaredInputs) {
        differentialDrive.arcadeDrive(x, y, squaredInputs);
    }

    /**
     * Arcade drive with acceleration-limiting, input ramping, and deadzone implementation
     */
    public static void defaultDrive(double x, double y) {
        drive(OI.checkXboxDeadzone(x), OI.checkXboxRightDeadzone(y), true);
    }

    // TODO: Different acceleration values for accel/decel/elevator position. Side-to-side different?
    public static void configLimitedAccelerationDrive() {
        leftFront.configOpenloopRamp(3, 0);
        leftBack.configOpenloopRamp(3, 0);
        rightFront.configOpenloopRamp(3, 0);
        rightBack.configOpenloopRamp(3, 0);
    }

    public static void removeLimitedAccelerationDrive() {
        leftFront.configOpenloopRamp(0, 0);
        leftBack.configOpenloopRamp(0, 0);
        rightFront.configOpenloopRamp(0, 0);
        rightBack.configOpenloopRamp(0, 0);
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

    private static int getLeftEncoderCount() {
        return leftEncoder.get();
    }

    private static int getRightEncoderCount() {
        return rightEncoder.get();
    }

    private static double getLeftEncoderDistance() {
        return leftEncoder.getDistance();
    }

    private static double getRightEncoderDistance() {
        return rightEncoder.getDistance();
    }

    // ------------ ANGLE TURNING / ROTATION PID METHODS ------------- //

    public static boolean initializeRotationPIDController() {

        if (rotationPIDController == null) {

            try {
                rotationPIDController = new PIDController(KP, KI, KD, navX, (output) -> rotationPIDOutput = -output);

                rotationPIDController.setAbsoluteTolerance(ROT_TOLERANCE_DEG);
                rotationPIDController.setInputRange(-180.0, 180.0);
                rotationPIDController.setOutputRange(-1.0, 1.0);
                rotationPIDController.setContinuous(true);
                rotationPIDController.disable();

            } catch (Exception e) {
                RobotLogger.err(instance.getClass(), "Error creating the DriveTrain Rotation PIDController!" + e.getMessage());
                return false;
            }

        }

        return true;

    }

    public static void enableRotationPIDController() {
        RobotLogger.log(instance.getClass(), "Enabling rotationPIDController");
        rotationPIDController.enable();
    }

    public static void disableRotationPIDController() {
        RobotLogger.log(instance.getClass(), "Disabling rotationPIDController");
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
    private static TankModifier tankModifier;
    private static final Trajectory.FitMethod fitMethod = Trajectory.FitMethod.HERMITE_CUBIC;
    private static final int samples = Trajectory.Config.SAMPLES_HIGH;
    private static final double timeStep = 0.05;         // time delta between points
    private static final double maxVelocity = 1.7;       // m/s // TODO: MEASURE
    private static final double maxAcceleration = 2.0;   // m/s/s
    private static final double maxJerk = 60.0;          // m/s/s/s

    private static final double wheelbaseWidth = 0.0;   // TODO: MEASURE (width of robot from left wheel to right wheel)

    private static void configPathfinder() {
        config = new Trajectory.Config(fitMethod, samples, timeStep, maxVelocity, maxAcceleration, maxJerk);
    }

    // TODO: TankModifier

    public static Trajectory generateTrajectory(Waypoint[] points) {
        return Pathfinder.generate(points, config);
    }

    public static void applyTrajectory(Trajectory trajectory) {
        tankModifier = new TankModifier(trajectory).modify(wheelbaseWidth);
        Trajectory leftTrajectory = tankModifier.getLeftTrajectory();
        Trajectory rightTrajectory = tankModifier.getRightTrajectory();

        EncoderFollower leftEnc = new EncoderFollower(leftTrajectory);
        EncoderFollower rightEnc = new EncoderFollower(rightTrajectory);

        leftEnc.configureEncoder(leftEncoder.get(), 2048, wheelbaseWidth);
        rightEnc.configureEncoder(rightEncoder.get(), 2048, wheelbaseWidth);

        leftEnc.configurePIDVA(1.0, 0, 0, 1 / maxVelocity, 0);
        rightEnc.configurePIDVA(1.0, 0, 0, 1 / maxVelocity, 0);

        double leftOutput = leftEnc.calculate(leftEncoder.get());   // Put in PID Controller
        double rightOutput = rightEnc.calculate(rightEncoder.get());

    }

    // ------------ AUXILIARY COMMANDS ------------- //

    public static Command disableLimitedAcceleration = new Command() {

        @Override
        protected void execute() {
            DriveTrain.removeLimitedAccelerationDrive();
        }

        @Override
        protected void end() {
            DriveTrain.configLimitedAccelerationDrive();
        }

        @Override
        protected boolean isFinished() {
            return OI.getXboxController().getBumperReleased(GenericHID.Hand.kRight);
        }

    };

}
