package org.whsrobotics.subsystems;

import org.whsrobotics.communications.ArduinoI2C;
import org.whsrobotics.utils.RobotLogger;
import edu.wpi.first.wpilibj.command.Subsystem;

/*
    This subsystem controls all functions on Arduino controlled connect to the robot.
    Functions controlled by Arduino are:
        1. LED Controls
        2. Read distance from the Ultrasonic sensor
 */



public class Arduino extends Subsystem {
    // this is the address we will use to communicate on the i2c wire between Rio and Arduino
    private static final int deviceAddress = 0X4;
    private static Arduino instance;
    private static ArduinoI2C i2c;

    // LED Control Commands ( We can add more based on need
    private static String LED_ON = "1";
    private static String LED_OFF = "0";


    /*
    We have to define command protocol between Rio and Arduino to do specific actions
    with Strip Lights or Ring Lights; like changing colors or fireworks etc.
    */
    private Arduino() {

        try {
            if (i2c == null) {
                i2c = new ArduinoI2C(deviceAddress);
            }

        } catch (NullPointerException e) {
            RobotLogger.err(this.getClass(), "Error instantiating the Led control!" + e.getMessage());
        }
    }

    public static Arduino getInstance() {
        if (instance == null) {
            instance = new Arduino();
        }
        return instance;
    }

    // Turn ON the LED Ring lights connected to Arduuno via I2C Wire.
    public void lightsOn() {
        if (i2c.isNotAddressable()) {
            RobotLogger.err(this.getClass(), "Unable to address Arduino!");
        } else {
            i2c.writeData(LED_ON.toCharArray());
        }
    }

    // Turn OFF the LED Ring lights connected to Arduuno via I2C Wire.
    public void lightsOff () {
        if (i2c.isNotAddressable()) {
                RobotLogger.err(this.getClass(), "Unable to address Arduino!");
        } else {
                i2c.writeData(LED_OFF.toCharArray());
        }
    }

    public double getDistance() {

        double distance = -1; // Error condition where sensor fails

        if (i2c.isNotAddressable()) {
            RobotLogger.err(this.getClass(), "Unable to address Arduino!");
        } else {
            String data = i2c.readData();
            // convert this to a double and send it out.
            distance = Double.valueOf(data);
        }
        return distance;
    }

    public void initDefaultCommand () {
            // Set the default command for a subsystem here.
    }
}
