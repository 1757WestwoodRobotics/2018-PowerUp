package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.SPI;

public class RobotMap {

    public enum DriveTrainTalon {
        LEFT_FRONT(5), LEFT_BACK(6), RIGHT_FRONT(7), RIGHT_BACK(8);

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