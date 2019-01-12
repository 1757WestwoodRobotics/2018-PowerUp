package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.*;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToExchange;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToScale;
import org.whsrobotics.commands.commandgroups.CGDeployCubeToSwitch;
import org.whsrobotics.commands.commandgroups.CGGrabCube;
import org.whsrobotics.subsystems.CubeGripper;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorVelocityMode;
import org.whsrobotics.utils.AutomationCanceler;
import org.whsrobotics.utils.RobotLogger;

import static org.whsrobotics.robot.RobotMap.*;

public class OI {

    private static XboxController xboxController;
    private static Joystick buttonBox;
    private static Joystick buttonBox1;
    private static Joystick flightStickLeft;
    private static Joystick flightStickRight;

    private static SendableChooser<Autonomous.FieldTarget> fieldTargetChooser;
    private static SendableChooser<Autonomous.StartingPosition> startingPositionChooser;
    private static SendableChooser<Elevator.Position> elevatorPositionChooser;
    private static SendableChooser<CubeGripper.Position> cubeGripperModeChooser;
    private static SendableChooser<CubeSpinner.Mode> cubeSpinnerModeChooser;

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        buttonBox = new Joystick(BUTTONBOX_PORT);
        buttonBox1 = new Joystick(BUTTONBOX_PORT1);
        flightStickLeft = new Joystick(FLIGHTSTICK_LEFT);
        flightStickRight = new Joystick(FLIGHTSTICK_RIGHT);

        // (new JoystickButton(xboxController, XboxButton.kB.value)).whenPressed(new DriveForward());  // TODO: Find Box
        // (new JoystickButton(xboxController, XboxButton.kY.value)).whenPressed(new DriveForward());  // TODO: Refl. Tape
        (new JoystickButton(xboxController, XboxButton.kBumperLeft.value)).whileHeld(new Command() {

            @Override
            protected void initialize() {
                DriveTrain.setBrakeMode();
            }

            @Override
            protected void end() {
                DriveTrain.setCoastMode();
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

        }); // TODO: Test. Switch to whileHeld?
        (new JoystickButton(xboxController, XboxButton.kBumperRight.value)).whileHeld(new Command() {

            @Override
            protected void initialize() {
                DriveTrain.removeDriveTrainAccelLimit();
            }

            @Override
            protected void end() {
                DriveTrain.setDriveTrainAccelLimit();
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

        }); // TODO: Test. Switch to whileHeld?
        (new JoystickButton(xboxController, XboxButton.kX.value)).whileHeld(new Command() {
            @Override
            protected boolean isFinished() {
                CubeGripper.resetEncoderPosition();
                return true;
            }
        });


        (new JoystickButton(xboxController, 1)).whenPressed(new CubeGripperApplyConstantVoltage());

        (new JoystickButton(xboxController, 2)).whenPressed(new MoveCubeGripper(CubeGripper.Position.ALMOST_FOLD));

        (new JoystickButton(xboxController, 10)).whenPressed(new MoveCubeGripper(CubeGripper.Position.FOLD_BACK));

        (new JoystickButton(xboxController, 4)).whenPressed(new MoveCubeGripper(CubeGripper.Position.OPEN_ARMS));

        (new JoystickButton(xboxController, 7)).whileHeld(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS));
        (new JoystickButton(xboxController, 7)).whenReleased(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

        (new JoystickButton(xboxController, 8)).whileHeld(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));
        (new JoystickButton(xboxController, 8)).whenReleased(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

        (new JoystickButton(xboxController, 9)).whenPressed(CubeGripper.disableOutputCommand);


        // (new JoystickButton(buttonBox, 1)).whenPressed(new CGGrabCube());
        (new JoystickButton(buttonBox, 2)).whenPressed(new CGDeployCubeToSwitch());
        (new JoystickButton(buttonBox, 3)).whenPressed(new CGDeployCubeToScale());
        (new JoystickButton(buttonBox, 4)).whenPressed(new CGDeployCubeToExchange());
        (new JoystickButton(buttonBox, 5)).whenPressed(new AutomationCanceler());   // TODO: Test. Does this even work?

        (new JoystickButton(buttonBox, 6)).whenPressed(new MoveCubeGripper(CubeGripper.Position.OPEN_ARMS));
        (new JoystickButton(buttonBox, 7)).whenPressed(new CubeGripperApplyConstantVoltage());
        (new JoystickButton(buttonBox, 8)).whenPressed(new MoveCubeGripper(CubeGripper.Position.ALMOST_FOLD)); //TODO: Sean's

        (new JoystickButton(buttonBox, 9)).whileHeld(new SpinCubeSpinner(CubeSpinner.Mode.INWARDS));
        (new JoystickButton(buttonBox, 9)).whenReleased(new SpinCubeSpinner(CubeSpinner.Mode.OFF));
        (new JoystickButton(buttonBox, 10)).whileHeld(new SpinCubeSpinner(CubeSpinner.Mode.OUTWARDS));
        (new JoystickButton(buttonBox, 10)).whenReleased(new SpinCubeSpinner(CubeSpinner.Mode.OFF));

        (new JoystickButton(buttonBox, 11)).whenPressed(CubeGripper.disableOutputCommand);

//        (new JoystickButton(buttonBox1, 12)).whileHeld(new Command() {
//            @Override
//            protected boolean isFinished() {
//                return false;
//            }
//        }); // Open Arms

        (new JoystickButton(buttonBox, 12)).whenPressed(new MoveCubeGripper(CubeGripper.Position.FOLD_BACK));
        (new JoystickButton(buttonBox1, 1)).whileHeld(new Command() {
            @Override
            protected boolean isFinished() {
                return false;
            }
        }); // Close Arms

        (new JoystickButton(buttonBox1, 2)).whileHeld(new Command() {

            @Override
            protected void execute() {
                Elevator.moveWithVelocity(0.7);
            }

            @Override
            protected void end() {
                Elevator.moveToValue(Elevator.getEncoderPosition());
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

        }); // Elevator Up
        (new JoystickButton(buttonBox1, 3)).whileHeld(new Command() {
            @Override
            protected void execute() {
                Elevator.moveWithVelocity(-0.2);
            }

            @Override
            protected void end() {
                Elevator.moveToValue(Elevator.getEncoderPosition());
            }

            @Override
            protected boolean isFinished() {
                return false;
            }
        }); // Elevator Down


        //This is the code for the "almost folded back" setting on the cube gripper

        //(new JoystickButton(buttonBox1, 4)).whenPressed(new MoveCubeGripper(CubeGripper.Position.ALMOST_FOLD));

        /*
        TODO: Sean: I think that we should make like 4 modes, which are controlled by the D-Pad
        TODO: Ex. Up goes to really fast mode, Right goes to fast mode, Left goes to slow mode, and down goes to normal
        */

        (new ElevatorVelocityMode()).whenActive(new MoveElevatorVelocity());    // Convert to LT/RT?

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

    private static final double XBOX_DEADZONE = 0.07;
    private static final double XBOX_RIGHT_DEADZONE = 0.08;

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

    /**
     * Curving for the left joystick (forwards/backwards)
     *
     * @param value
     * @return
     */
    public static double leftXboxJoystickCurve(double value) {

        if (Math.abs(value) >= XBOX_DEADZONE) {
            // Raises the raw xbox input to the 3rd power and adds 10%
            return Math.copySign(Math.pow(value, 3) + 0.10, value);
        }

        return 0;

    }

    /**
     * Curving for the right joystick (used for turning)
     *
     * @param value
     * @return
     */
    public static double rightXboxJoystickCurve(double value) {

        if (Math.abs(value) >= XBOX_RIGHT_DEADZONE) {
            // Raises the (xbox input / 1.2) to the 2nd power and adds 10%. Full joystick = ~80%
            return Math.copySign(Math.pow((value / 1.2), 2) + 0.10, value);
        }

        return 0;

    }

    // ------------ FLIGHTSTICK METHODS ------------- //

    public static Joystick getFlightStickLeft() {
        return flightStickLeft;
    }

    public static Joystick getFlightStickRight() {
        return flightStickRight;
    }


    // ------------ AUTONOMOUS METHODS ------------- //

    private void publishAutonomous() {

        // Field Target (Manual)
        fieldTargetChooser = new SendableChooser<>();
        fieldTargetChooser.addDefault("Default - AUTO_SWITCH", Autonomous.FieldTarget.AUTO_SWITCH);

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
        RobotLogger.getInstance().log(instance.getClass(), "getSelectedElevatorPosition");
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
        cubeGripperModeChooser.addDefault("Default - OPEN_ARMS", CubeGripper.Position.OPEN_ARMS);

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
                RobotLogger.getInstance().err(instance.getClass(), "Error with getting the Alliance Data! " + e.getMessage(), false);
            }
        }

        return alliance;
    }

}