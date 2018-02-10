package org.whsrobotics.communications;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import org.whsrobotics.utils.RobotLogger;


public class ArduinoI2C {
    private I2C i2c;

    public  ArduinoI2C(int deviceAddress) {
        i2c = new I2C(Port.kOnboard, deviceAddress);
    }

    public boolean writeData(char[] charArray){


        // convert char stream to byte stream
        byte[] WriteData = new byte[charArray.length];
        byte[] ReadData = new byte[0];

        for (int i = 0; i < charArray.length; i++) {
            WriteData[i] = (byte) charArray[i];
        }
        RobotLogger.log(this.getClass(), new String(charArray));
        return i2c.transaction(WriteData, WriteData.length, ReadData, 0);
    }
}
