package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

    public enum DriveTrainTalon {
        LEFT_FRONT(1), LEFT_BACK(3), RIGHT_FRONT(2), RIGHT_BACK(4);

        private int port;

        DriveTrainTalon(int port) {
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