package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.communications.ArduinoI2C;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This subsystem controls all functions on an Arduino connect to the robot.
 * Functions controlled by Arduino are:
 *  1. LED Controls
 *  2. Read distance from the Ultrasonic sensor
 */
public class Arduino extends Subsystem {
    // this is the address we will use to communicate on the i2c wire between Rio and Arduino
    private static final int deviceAddress = 0X4;
    private static Arduino instance;
    private static ArduinoI2C i2c;
    private static DigitalOutput resetButton;

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
        StripLEDsOff("18"),
        RingLEDSPulse("19"),
        StripLEDsPulse("20");

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
                Send(Command.StripLEDsWhite);    // Start with the command to make the LED Strips white --> Pulse?
            }
            if (resetButton == null) {
                resetButton = new DigitalOutput(RobotMap.DigitalInputPort.ARDUINO_RESET.port); // use channel 0 to control Arduino reset
                resetButton.set(true); // Keep the reset pin high
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

    // Method to reset Arduino via DIO channel 0
    public static void resetArduino() {
        resetButton.set(false); // take the reset pin low
        Timer.delay(0.2);
        resetButton.set(true); // take the reset pin high
    }


    /*
     * Method to control LED lights connected to Arduino. 
     * Based on LED Control Commands control the LED Ring lights connected to Arduino via I2C Wire.
     */
    
    public synchronized void Send(Command cmd) {
        i2c.writeData(cmd.value.toCharArray());
    }
    
    /*
     * Method to retrieve distance of the near object the ultrasonic sensor
     * connected to Arduino can see.
     *
     * Return: distance measured in cm.
     */
    public synchronized double getDistance() {

        double distance = -1;   // Error condition where sensor fails
        int count = 100;        // Try 100 times and then abort
        boolean printErrMsg = true; // Only prints the error message once per call (doesn't loop)

//        if (i2c.isNotAddressable()) {
//            RobotLogger.getInstance().err(this.getClass(), "Unable to address Arduino!", false);
//        } else {

        while ((distance == -1) && (count > 0)) {

            String data = i2c.readData();

            System.out.println( "Got distance - " + data);

            // convert this to a double
            try {
                distance = Double.valueOf(data);
            } catch (Exception e) {
                if (printErrMsg) {
                    RobotLogger.getInstance().err(instance.getClass(), "#### Empty/Bad Ultrasonic String!", false);
                    printErrMsg = false;
                }
            }

            // Timer.delay(0.2);
            --count; // decrement count

        }

        return distance;    // returns -1 if fails

    }

    public void initDefaultCommand () {
            // Set the default command for a subsystem here.
    }

    public void onDisabledInit() {
        Send(Command.StripLEDsWhite);
    }

}
