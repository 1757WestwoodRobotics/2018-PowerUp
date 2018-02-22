package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.RobotLogger;

/**
 * Code that runs during Autonomous
 */
public class Autonomous {

    private static String gameData = "";
    private static DriverStation.Alliance alliance = DriverStation.Alliance.Invalid;

    public enum FieldTarget {
        CROSS_LINE, PC_ZONE, EXCHANGE,
        LEFT_SWITCH, RIGHT_SWITCH, LEFT_SCALE, RIGHT_SCALE;
    }

    public enum StartingPosition {
        LEFT, MIDDLE, RIGHT;
    }

    private static Autonomous instance;

    private Autonomous() {

    }

    public static Autonomous getInstance() {
        if (instance == null) {
            instance = new Autonomous();
        }
        return instance;
    }

    public static String getGameData() {
        if (gameData.isEmpty()) {
            try {
                gameData = DriverStation.getInstance().getGameSpecificMessage();
            } catch (Exception e) {
                RobotLogger.err(instance.getClass(), "Error with getting the Game Data! " + e.getMessage());
            }
        }

        return gameData;
    }

    public static DriverStation.Alliance getAlliance() {
        if (alliance == DriverStation.Alliance.Invalid) {
            try {
                alliance = DriverStation.getInstance().getAlliance();
            } catch (Exception e) {
                RobotLogger.err(instance.getClass(), "Error with getting the Alliance Data! " + e.getMessage());
            }
        }

        return alliance;
    }

}
