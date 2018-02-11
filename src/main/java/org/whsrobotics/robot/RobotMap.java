package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

    // TODO: Fix port numbers
    public enum MotorControllerPorts {
        DRIVE_LEFT_FRONT(5), DRIVE_LEFT_BACK(6), DRIVE_RIGHT_FRONT(7), DRIVE_RIGHT_BACK(8),
        ELEVATOR_LEFT(9), ELEVATOR_RIGHT(10),
        GRIPPER_LEFT(11), GRIPPER_RIGHT(12),
        SPINNER_LEFT(12), SPINNER_RIGHT(13);

        private int port;

        MotorControllerPorts(int port) {
            this.port = port;
        }

        public int getPort() {
            return this.port;
        }

    }

    public static final SPI.Port NAVX_PORT = SPI.Port.kMXP;

    public static final int XBOX_PORT = 0;

    public static void init() {

    }

}