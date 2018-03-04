package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.communications.JetsonLAN;
import org.whsrobotics.utils.RobotLogger;

import java.util.Arrays;

public class Vision {

    private static String camera1URL;
    private static String camera2URL;

    private static Rcommand rcommand;
    private static Jcommand jcommand;

    private static double box1angle;
    private static double box1distance;

    private static double refltargangle;
    private static double refltargdistance;

    private static Vision instance;

    public enum Rcommand {
        Standby(0), VerifyConnection(1),
        FindBox(2), FindReflTarg(3),
        Restart(4), Shutdown(5);

        public int value;

        Rcommand(int value) {
            this.value = value;
        }

        public static Rcommand lookupRcommand(int value) {
            return Arrays.stream(Rcommand.values())
                    .filter(command -> command.value == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Not a valid Rcommand value!" + value));
        }
    }

    public enum Jcommand {
        Standby(0), ErrorWithConnection(1),
        FindingBox(2), FindingBoxError(3), FindingReflTarg(4), FindingReflTargError(5),
        Camera1Error(6), Camera2Error(7),
        HardwareError(8), Restarting(9), ShuttingDown(10);

        private int value;

        Jcommand(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Jcommand lookupJcommand(int value) {
            return Arrays.stream(Jcommand.values())
                    .filter(command -> command.value == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Not a valid Jcommand value!" + value));
        }
    }

    private Vision() {
        CameraServer.getInstance().startAutomaticCapture();
    }

    public static Vision getInstance(){

        if (instance == null) {
            instance = new Vision();

        }

        return instance;
    }

    // Jetson stuff

    public static void sendRcommand(Rcommand command) {
        rcommand = command;
        JetsonLAN.writeData("Rcommand", rcommand.value);
        SmartDashboard.putString("Jetson Rcommand", rcommand.toString());
    }

    public static void receiveJcommand(int value) {
        jcommand = Jcommand.lookupJcommand(value);

        SmartDashboard.putString("Jetson Jcommand", jcommand.toString());
        RobotLogger.getInstance().log(instance.getClass(), "##### Received Jetson Jcommand: " + value + " " + jcommand);

        // TODO: Handle Jcommand
    }

}