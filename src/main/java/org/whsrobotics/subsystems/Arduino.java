package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
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
    public enum Command{
        AllLEDsOff("0"),
        RingLEDsRed("1"),
        RingLEDsGreen("2"),
        RingLEDsOrange("3"),
        RingLEDsYellow("4"),
        RingLEDsBlue("5"),
        RingLEDsWhite("6"),
        RingLEDsOff("7"),
        StripLED20vHigh("8"),
        StripLEDs20vMed("9"),
        StripLEDs20vLow("10"),
        StripLEDs20vOff("11"),
        StripLEDsRed("12"),
        StripLEDsGreen("13"),
        StripLEDsOrange("14"),
        StripLEDsYellow("15"),
        StripLEDsBlue("16"),
        StripLEDsWhite("17"),
        StripLEDsOff("18");

       public String value;
       Command (String value) {this.value = value;}
    }

    /*
     * We have to define command protocol between Rio and Arduino to do specific actions
     * with Strip Lights or Ring Lights; like changing colors or fireworks etc.
    */
    private Arduino() {

        try {
            if (i2c == null) {
                i2c = new ArduinoI2C(deviceAddress);
                Send(Command.StripLEDs20vMed);    // Start with the command to make the LED Strips white --> Pulse?
            }

        } catch (NullPointerException e) {
            RobotLogger.getInstance().err(this.getClass(), "Error instantiating the Arduino control!" + e.getMessage(), true);
        }

    }

    public static Arduino getInstance() {
        if (instance == null) {
            instance = new Arduino();
        }
        return instance;
    }

    /*
     * Method to control LED lights connected to Arduino. 
     * Based on LED Control Commands control the LED Ring lights connected to Arduino via I2C Wire.
     */
    
    public void Send(Command cmd) {
        i2c.writeData(cmd.value.toCharArray());
    }
    
    /*
     * Method to retrieve distance of the near object the ultrasonic sensor
     * connected to Arduino can see.
     *
     * Return: distance measured in inches.
     */
    public double getDistance() {

        double distance = -1; // Error condition where sensor fails

        if (i2c.isNotAddressable()) {
            RobotLogger.getInstance().err(this.getClass(), "Unable to address Arduino!", false);
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

    public void onDisabledInit() {
        Send(Command.StripLEDs20vLow);
    }

}
