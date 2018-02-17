package org.whsrobotics.communications;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class JetsonLAN {

    NetworkTableInstance instance = NetworkTableInstance.getDefault();
    NetworkTable table = instance.getTable("Jetson");

    // https://wpilib.screenstepslive.com/s/currentCS/m/75361/l/843364-listening-for-value-changes

}
