package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.*;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToScale;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToSwitch;
import org.whsrobotics.commands.commandgroups.CGGrabCube;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorVelocityMode;
import org.whsrobotics.utils.RobotLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import static org.whsrobotics.robot.RobotMap.XBOX_PORT;

public class OI {

    private static XboxController xboxController;

    private static SendableChooser<Autonomous.FieldTarget> fieldTargetChooser;
    private static SendableChooser<Autonomous.StartingPosition> startingPositionChooser;
    private static SendableChooser<Elevator.Position> elevatorPositionChooser;
    private static SendableChooser<CubeGripper.Position> cubeGripperModeChooser;
    private static SendableChooser<CubeSpinner.Mode> cubeSpinnerModeChooser;

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        (new JoystickButton(xboxController, XboxButton.kA.value)).whenPressed(new CGGrabCube());
        (new JoystickButton(xboxController, XboxButton.kB.value)).whenPressed(new CGDeployCubeToSwitch());
        (new JoystickButton(xboxController, XboxButton.kX.value)).whenPressed(new CGDeployCubeToScale());

        (new JoystickButton(xboxController, XboxButton.kBumperRight.getValue())).whenPressed(DriveTrain.disableLimitedAcceleration);
        (new ElevatorVelocityMode()).whenActive(new MoveElevatorVelocity());

        publishElevator();
        publishCubeSpinner();
        publishCubeGripper();
        publishAutonomous();

        SmartDashboard.putData("CGGrabCube", new CGGrabCube());
        SmartDashboard.putData("CGDeployCubeToScale", new CGDeployCubeToScale());
        SmartDashboard.putData("CGDeployCubeToSwitch", new CGDeployCubeToSwitch());

    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }

        return instance;
    }


    // ------------ XBOX CONTROLLER ------------- //

    private static final double XBOX_DEADZONE = 0.05;
    private static final double XBOX_RIGHT_DEADZONE = 0.1;

    private enum XboxButton {
        kBumperLeft(5),
        kBumperRight(6),
        kStickLeft(9),
        kStickRight(10),
        kA(1),
        kB(2),
        kX(3),
        kY(4),
        kBack(7),
        kStart(8);

        private int value;

        XboxButton(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

    public static XboxController getXboxController() {
        return xboxController;
    }

    public static double checkXboxDeadzone(double value) {

        if (Math.abs(value) >= XBOX_DEADZONE) {
            return value;
        }

        return 0;

    }

    public static double checkXboxRightDeadzone(double value) {

        if (Math.abs(value) >= XBOX_RIGHT_DEADZONE) {
            return value;
        }

        return 0;

    }


    // ------------ AUTONOMOUS METHODS ------------- //

    private void publishAutonomous() {

        // Field Target (Manual)
        fieldTargetChooser = new SendableChooser<>();
        fieldTargetChooser.addDefault("Default - CODE_DECISION", Autonomous.FieldTarget.CODE_DECISION);

        for (Autonomous.FieldTarget target : Autonomous.FieldTarget.values()) {
            fieldTargetChooser.addObject(target.toString(), target);
        }

        SmartDashboard.putData("Manual Field Target Chooser", fieldTargetChooser);

        // Robot Starting position
        startingPositionChooser = new SendableChooser<>();
        startingPositionChooser.addDefault("Default - LEFT", Autonomous.StartingPosition.LEFT);

        for (Autonomous.StartingPosition position : Autonomous.StartingPosition.values()) {
            startingPositionChooser.addObject(position.toString(), position);
        }

        SmartDashboard.putData("Starting Position Chooser", startingPositionChooser);

    }

    public static Autonomous.FieldTarget getSelectedAutoFieldTarget() {
        return fieldTargetChooser.getSelected();
    }

    public static Autonomous.StartingPosition getSelectedAutoStartingPosition() {
        return startingPositionChooser.getSelected();
    }


    // ------------ ELEVATOR METHODS ------------- //

    private static void publishElevator() {
        elevatorPositionChooser = new SendableChooser<>();
        elevatorPositionChooser.addDefault("Default - DOWN", Elevator.Position.DOWN);

        for (Elevator.Position position : Elevator.Position.values()) {
            elevatorPositionChooser.addObject(position.toString(), position);
        }

        SmartDashboard.putData("Elevator Chooser", elevatorPositionChooser);

        SmartDashboard.putData("Elevator - Position", new MoveElevatorDS());

        SmartDashboard.putData("Reset Elevator Encoders", Elevator.resetEncoderPositionCommand);

    }

    public static Elevator.Position getSelectedElevatorPosition() {
        System.out.println("getSelectedElevatorPosition");
        return elevatorPositionChooser.getSelected();
    }


    // ------------ CUBE SPINNER METHODS ------------- //

    private static void publishCubeSpinner() {
        cubeSpinnerModeChooser = new SendableChooser<>();
        cubeSpinnerModeChooser.addDefault("Default - OFF", CubeSpinner.Mode.OFF);

        for (CubeSpinner.Mode mode : CubeSpinner.Mode.values()) {
            cubeSpinnerModeChooser.addObject(mode.toString(), mode);
        }

        SmartDashboard.putData("CubeSpinner Chooser", cubeSpinnerModeChooser);
        SmartDashboard.putData("CubeSpinner Button", new SpinCubeSpinnerDS());
    }

    public static CubeSpinner.Mode getSelectedCubeSpinnerMode() {
        return cubeSpinnerModeChooser.getSelected();
    }


    // ------------ CUBE GRIPPER METHODS ------------- //

    private static void publishCubeGripper() {
        cubeGripperModeChooser = new SendableChooser<>();
        cubeGripperModeChooser.addDefault("Default - SWITCH", CubeGripper.Position.MIDDLE);

        for (CubeGripper.Position position : CubeGripper.Position.values()) {
            cubeGripperModeChooser.addObject(position.toString(), position);
        }

        SmartDashboard.putData("CubeGripper Chooser", cubeGripperModeChooser);
        SmartDashboard.putData("CubeGripper Button", new MoveCubeGripperDS());

        SmartDashboard.putData("Disable CubeGripper Mode", CubeGripper.disableOutputCommand);
        SmartDashboard.putData("Reset CubeGripper Encoders", CubeGripper.resetEncoderPositionCommand);
        SmartDashboard.putData("CubeGripper Constant Voltage", new CubeGripperApplyConstantVoltage());

    }

    public static CubeGripper.Position getSelectedCubeGripperPosition() {
        return cubeGripperModeChooser.getSelected();
    }


    // ------------ DRIVER STATION / FMS ------------- //

    private static DriverStation.Alliance alliance = DriverStation.Alliance.Invalid;

    public static DriverStation.Alliance getAlliance() {
        if (alliance == DriverStation.Alliance.Invalid) {
            try {
                alliance = DriverStation.getInstance().getAlliance();
            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error with getting the Alliance Data! " + e.getMessage());
            }
        }

        return alliance;
    }

}