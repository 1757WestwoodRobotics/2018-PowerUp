package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.whsrobotics.robot.RobotMap;

public class DriveTrain extends Subsystem {

    private static WPI_TalonSRX leftFront;
    private static WPI_TalonSRX leftBack;
    private static WPI_TalonSRX rightFront;
    private static WPI_TalonSRX rightBack;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static AHRS navX;
    private static PIDController rotationPIDController;

    private static final double KP = 0.05;   // Tuned values for the test robot (Pneumatic wheels)
    private static final double KI = 0;
    private static final double KD = 0.125;

    private static double rotationPIDOutput;
    private static final double ROT_TOLERANCE_DEG = 0.5f;

    private static DriveTrain instance;

    private DriveTrain() {

        try {
            leftFront = new WPI_TalonSRX(RobotMap.DriveTrainTalon.LEFT_FRONT.getPort());
            leftBack = new WPI_TalonSRX(RobotMap.DriveTrainTalon.LEFT_BACK.getPort());
            rightFront = new WPI_TalonSRX(RobotMap.DriveTrainTalon.RIGHT_FRONT.getPort());
            rightBack = new WPI_TalonSRX(RobotMap.DriveTrainTalon.RIGHT_BACK.getPort());

            leftDrive = new SpeedControllerGroup(leftFront, leftBack);
            rightDrive = new SpeedControllerGroup(rightFront, rightBack);

            differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

            navX = new AHRS(RobotMap.NAVX_PORT);

        } catch (NullPointerException e) {
            System.err.println("Error instantiating the DriveTrain hardware" + e.getMessage());
            DriverStation.reportError("Error instantiating the DriveTrain hardware!", true);

        }

        try {
            rotationPIDController = new PIDController(KP, KI, KD, navX, (output) -> rotationPIDOutput = -output);

            rotationPIDController.setAbsoluteTolerance(ROT_TOLERANCE_DEG);
            rotationPIDController.setInputRange(-180.0, 180.0);
            rotationPIDController.setOutputRange(-1.0, 1.0);
            rotationPIDController.setContinuous(true);
            rotationPIDController.disable();

        } catch (Exception e) {
            System.err.println("Error creating the DriveTrain Rotation PIDController!" + e.getMessage());
            DriverStation.reportError("Error creating the DriveTrain Rotation PIDController!", true);

        }

        resetNavXYaw();

    }

    public static DriveTrain getInstance() {
        if (instance == null) {
            instance = new DriveTrain();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }

    // DriveTrain methods

    public static void drive(double x, double y, boolean squaredInputs) {
        differentialDrive.arcadeDrive(x, y, squaredInputs);
    }

    public static void turnToAngle(double speed, double angle) {
        rotationPIDController.setSetpoint(angle);
        drive(speed, rotationPIDOutput, false);
    }

    public static void stopDrive() {
        differentialDrive.stopMotor();
    }

    // NavX methods

    public static void resetNavXYaw() {
        navX.reset();
    }

    public static double getYawAngle() {
        return navX.getYaw();
    }

    // Rotation PID methods

    public static void enableRotationPIDController() {
        rotationPIDController.enable();
    }

    public static void disableRotationPIDController() {
        rotationPIDController.disable();
    }

    public static boolean isRotationPIDControllerOnTarget() {
        return rotationPIDController.onTarget();
    }

    public static double getRotationPIDControllerSetpoint() {
        return rotationPIDController.getSetpoint();
    }

}
