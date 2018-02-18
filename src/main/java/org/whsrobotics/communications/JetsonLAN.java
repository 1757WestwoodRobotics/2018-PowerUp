package org.whsrobotics.communications;

import edu.wpi.first.networktables.*;

public class JetsonLAN {

    private static NetworkTableInstance instance;
    private static NetworkTable table;

    public JetsonLAN() {
        instance = NetworkTableInstance.getDefault();
        table = instance.getTable("Jetson");

        table.addEntryListener("boxX", (table, key, entry, value, flags) -> {

        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    }

    public static NetworkTableValue getValue(String key) {
        return table.getEntry(key).getValue();
    }

    public static void writeData(String key, Object data) {
        table.getEntry(key).setValue(data);
    }

    public static void waitForDataChange() {

    }

    // https://wpilib.screenstepslive.com/s/currentCS/m/75361/l/843364-listening-for-value-changes

}
