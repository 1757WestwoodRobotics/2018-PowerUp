# Folder contains code to controll LED lights and Ultrosound Sensor connect to Ardino. Comminicaiton is via I2C

PIN Connections on Arduino (Digital Connector Side)

I2C Connectivity - Make sure to connect GND. If not communication will not work.

|Arduino     |    RoboRio   |
|-------     |    -------   |
|(I2C)SDA    |    I2C SDA   |
|(I2C)SCL    |    I2C SCL   |
|GND         |    I2C GND   |

|Arduino     |    RoboRio   |
|-------     |    -------   |
|Data Pin 6  |   Data In    |
|VCC 5V      |   VCC        |
|GND         |   GND        |


|Arduino     |    Ultrasound Sensor |
|-------     |    ----------------- |
| Pin 12     |    Trigger           |
| Pin 11     |    Echo



