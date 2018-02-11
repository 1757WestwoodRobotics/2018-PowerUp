package org.whsrobotics.subsystems;

import org.whsrobotics.communications.ArduinoI2C;
import org.whsrobotics.utils.RobotLogger;

/*
    LED Utility class to control light from the robot.
 */


public class Led {
    // this is the address we will use to communicate on the i2c wire between Rio and Arduino
    private static final int ledAddress = 4;
    private static Led instance;
    private static ArduinoI2C arduino;

    /*
    We have to define command protocol between Rio and Arduino to do specific actions
    with Strip Lights or Ring Lights; like changing colors or fireworks etc.
    */
    private Led() {

        try {
            if (arduino == null) {
                arduino = new ArduinoI2C(ledAddress);
            }

        } catch (NullPointerException e) {
            RobotLogger.err(this.getClass(), "Error instantiating the Led control!" + e.getMessage());
        }
    }
    public static Led getInstance() {
        if (instance == null) {
            instance = new Led();
        }

        return instance;
    }
}
