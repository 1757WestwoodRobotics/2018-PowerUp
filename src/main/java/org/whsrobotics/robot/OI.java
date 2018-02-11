package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.whsrobotics.commands.DefaultDrive;

import static org.whsrobotics.robot.RobotMap.XBOX_PORT;

public class OI {

    private static XboxController xboxController;
    private static final double XBOX_DEADZONE = 0.05;

    private static OI instance;

    private OI() {
        xboxController = new XboxController(XBOX_PORT);
        // (new JoystickButton(xboxController, 0)).whenPressed(new DefaultDrive());
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