package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.DefaultDrive;
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

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        // (new JoystickButton(xboxController, 0)).whenPressed(new DefaultDrive());

        (new ElevatorVelocityMode()).whenActive(new MoveElevatorVelocity());

        SmartDashboard.putData("Elevator - Move DS", new MoveElevatorDS((int) SmartDashboard.getNumber("Target Elevator Position", 0)));
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

}