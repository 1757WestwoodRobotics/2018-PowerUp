package org.whsrobotics.subsystems;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.UsbCameraInfo;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.communications.JetsonLAN;
import org.whsrobotics.utils.RobotLogger;

import java.util.Arrays;

public class Vision {

//    private static String camera1URL;
//    private static String camera2URL;

    private static Rcommand rcommand;
    private static Jcommand jcommand;

//    private static double box1angle;
//    private static double box1distance;
//
//    private static double refltargangle;
//    private static double refltargdistance;

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

    public enum TableValues {
        box1angle, box1distance, refltargangle, refltargdistance
    }

    private Vision() {
        try {
                UsbCameraInfo[] cameras = UsbCamera.enumerateUsbCameras();
                if (cameras.length > 0) {
                    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(cameras[0].dev);  // Only if camera is connected to the RoboRIO!
                    camera.setVideoMode(new VideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 30));

//                    MjpegServer server = (MjpegServer) CameraServer.getInstance().getServer();  // TODO: NEED TO TEST!!!
//                    RobotLogger.getInstance().log(instance.getClass(), "Camera connected at ADDRESS: " + server.getListenAddress() + " PORT: " + server.getPort());
                } else {
                    RobotLogger.getInstance().err(instance.getClass(), "No cameras found!", false);
                    // Billy was here
                }

        } catch (Exception e) {
            RobotLogger.getInstance().err(instance.getClass(), "Cannot create cameras " + e.getMessage(), false);
        }

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

    private static double getEntryAsDouble(TableValues tableValue) {
        try {
            return JetsonLAN.getValue(tableValue.toString()).getDouble();
        } catch (ClassCastException e) {
            RobotLogger.getInstance().err(instance.getClass(), "##### Error casting to double for " + tableValue.toString(), false);
        }

        return -1;

    }


    public static double getBox1angle() {
        return getEntryAsDouble(TableValues.box1angle);
    }

    public static double getBox1distance() {
        return getEntryAsDouble(TableValues.box1distance);
    }

    public static double getRefltargangle() {
        return getEntryAsDouble(TableValues.refltargangle);
    }

    public static double getRefltargdistance() {
        return getEntryAsDouble(TableValues.refltargdistance);
    }


}