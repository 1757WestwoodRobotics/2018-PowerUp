package org.whsrobotics.communications;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import org.whsrobotics.utils.RobotLogger;

/**
 * I2C Utility class to send and read data from Arduino. This is a wrapper around the WPILib I2C class.
 */
public class ArduinoI2C {

    private I2C wire;
    private int address;
    private static final int MAX_BYTES = 32;

    public ArduinoI2C(int deviceAddress) {
        address = deviceAddress;
        wire = new I2C(Port.kOnboard, address);
    }

    public boolean isNotAddressable() {
        return wire.addressOnly();
    }

    /*
    Here Roborio is the master and Arduino is the slave
    If write is aborted, return will be false else true
    */
    public boolean writeData(char[] charArray){

        // convert char stream to byte stream
        byte[] WriteData = new byte[charArray.length];
        boolean failed;

        for (int i = 0; i < charArray.length; i++) {
            WriteData[i] = (byte) charArray[i];
        }

        RobotLogger.log(this.getClass(), "#### Writing message: " + new String(charArray));

        failed = wire.writeBulk(WriteData, WriteData.length);
        return failed;
    }

    public String readData() {

        byte[] data = new byte[MAX_BYTES];//create a byte array to hold the incoming data
        String returnData;

        boolean failed;

        failed = wire.read(address, MAX_BYTES, data);//use address 4 on i2c and store it in data

        if (failed) {
            return "";
        }
        else {
            return new String(data);//create a string from the byte array
        }
    }
}
