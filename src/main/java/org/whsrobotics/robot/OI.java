package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.MoveElevatorDS;
import org.whsrobotics.commands.MoveElevatorPosition;
import org.whsrobotics.commands.MoveElevatorVelocity;
import org.whsrobotics.subsystems.Elevator;
import org.whsrobotics.triggers.ElevatorTopLimit;
import org.whsrobotics.triggers.ElevatorVelocityMode;

import static org.whsrobotics.robot.RobotMap.XBOX_PORT;

public class OI {

    private static XboxController xboxController;
    private static final double XBOX_DEADZONE = 0.05;

    private static SendableChooser<Elevator.Position> elevatorPositionChooser;

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        // (new JoystickButton(xboxController, 0)).whenPressed(new DefaultDrive());

        (new ElevatorVelocityMode()).whenActive(new MoveElevatorVelocity());

        publishPositionChooser();

        // SmartDashboard buttons
        SmartDashboard.putData("Elevator - Move DS", new MoveElevatorDS(getManualTargetElevatorPosition()));
        SmartDashboard.putData("Elevator - Position", new MoveElevatorPosition(getSelectedElevatorPosition()));
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }

        return instance;
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

    private static void publishPositionChooser() {
        elevatorPositionChooser = new SendableChooser<>();
        elevatorPositionChooser.addDefault("Elevator - Down", Elevator.Position.DOWN);

        for (Elevator.Position position : Elevator.Position.values()) {
            elevatorPositionChooser.addDefault(position.toString(), position);
        }

        SmartDashboard.putData("Elevator Position", elevatorPositionChooser);
    }

    public static Elevator.Position getSelectedElevatorPosition() {
        return elevatorPositionChooser.getSelected();
    }

    public static int getManualTargetElevatorPosition() {
        return (int) SmartDashboard.getNumber("Elevator Target Position", Elevator.Position.DOWN.getTarget());
    }

}