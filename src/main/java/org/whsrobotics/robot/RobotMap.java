package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

    // TODO: Fix port numbers
    public enum MotorControllerPort {
        DRIVE_LEFT_FRONT(5), DRIVE_LEFT_BACK(6), DRIVE_RIGHT_FRONT(7), DRIVE_RIGHT_BACK(8),
        ELEVATOR_LEFT(9), ELEVATOR_RIGHT(10),
        GRIPPER_LEFT(11), GRIPPER_RIGHT(13),
        SPINNER_LEFT(12), SPINNER_RIGHT(14);

        public int port;

        MotorControllerPort(int port) {
            this.port = port;
        }
    }

    public enum DigitalInputPort {
        ELEVATOR_BOTTOM(0), ELEVATOR_TOP(1),
        ENCODER_LEFT_A(2), ENCODER_LEFT_B(3), ENCODER_LEFT_INDEX(4),
        ENCODER_RIGHT_A(5), ENCODER_RIGHT_B(6), ENCODER_RIGHT_INDEX(7);

        public int port;

        DigitalInputPort(int port) {
            this.port = port;
        }
    }

    public static final SPI.Port NAVX_PORT = SPI.Port.kMXP;

    public static final int XBOX_PORT = 0;

    public static void init() {

    }

}