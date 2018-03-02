package org.whsrobotics.communications;

import edu.wpi.first.networktables.*;
import org.whsrobotics.robot.Robot;
import org.whsrobotics.subsystems.Vision;
import org.whsrobotics.utils.RobotLogger;

public class JetsonLAN {

    private static NetworkTableInstance instance;
    private static NetworkTable table;

    public JetsonLAN() {
        instance = NetworkTableInstance.getDefault();
        table = instance.getTable("Jetson");

        addNTListeners();

    }

    public static NetworkTableValue getValue(String key) {
        return table.getEntry(key).getValue();
    }

    public static void writeData(String key, Object data) {
        table.getEntry(key).setValue(data);
    }

//    public static void waitForDataChange() {
//
//    }

    // https://wpilib.screenstepslive.com/s/currentCS/m/75361/l/843364-listening-for-value-changes

    private static void addNTListeners() {
        table.addEntryListener("Jcommand",
                (table, key, entry, value, flags) -> Vision.receiveJcommand((int) value.getDouble()),
                EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

        // TODO: Implement the other commands, set to Vision variables
    }

    private static void sendRcommand(Vision.Rcommand rcommand) {
        writeData("Rcommand", rcommand.value);
        RobotLogger.getInstance().log(instance.getClass(), "##### Sending Jetson Rcommand: " + rcommand.toString());
    }

}
