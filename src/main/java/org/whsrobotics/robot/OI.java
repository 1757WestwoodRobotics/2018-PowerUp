package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.*;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToScale;
import org.whsrobotics.commands.commandgroups.CGGrabCube;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorVelocityMode;

import static org.whsrobotics.robot.RobotMap.XBOX_PORT;

public class OI {

    private static XboxController xboxController;

    private static SendableChooser<Elevator.Position> elevatorPositionChooser;
    private static SendableChooser<CubeGripper.Position> cubeGripperModeChooser;
    private static SendableChooser<CubeSpinner.Mode> cubeSpinnerModeChooser;

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        // (new JoystickButton(xboxController, 0)).whenPressed(new DefaultDrive());

        (new JoystickButton(xboxController, XboxButton.kBumperRight.getValue())).whenPressed(new Command() {

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
                return xboxController.getBumperReleased(GenericHID.Hand.kRight);
            }

        });

        (new ElevatorVelocityMode()).whenActive(new MoveElevatorVelocity());

        publishElevator();
        publishCubeSpinner();
        publishCubeGripper();

        SmartDashboard.putData("CGGrabCube", new CGGrabCube());
        SmartDashboard.putData("CGDeployCubeToScale", new CGDeployCubeToScale());

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


    // ------------ ELEVATOR METHODS ------------- //

    private static void publishElevator() {
        elevatorPositionChooser = new SendableChooser<>();
        elevatorPositionChooser.addDefault("Default - DOWN", Elevator.Position.DOWN);

        for (Elevator.Position position : Elevator.Position.values()) {
            elevatorPositionChooser.addObject(position.toString(), position);
        }

        SmartDashboard.putData("Elevator Chooser", elevatorPositionChooser);

        SmartDashboard.putData("Elevator - Manual Entry", MoveElevatorDS.getInstance());
        SmartDashboard.putData("Elevator - Position", new MoveElevatorPosition(getSelectedElevatorPosition()));

    }

    public static Elevator.Position getSelectedElevatorPosition() {
        return elevatorPositionChooser.getSelected();
    }

    public static int getManualTargetElevatorPosition() {
        return (int) SmartDashboard.getNumber("Elevator Target Position", Elevator.Position.DOWN.getTarget());
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
        SmartDashboard.putData("CubeGripper Constant Voltage", CubeGripper.applyConstantVoltageCommand);

    }

    public static CubeGripper.Position getSelectedCubeGripperPosition() {
        return cubeGripperModeChooser.getSelected();
    }

}