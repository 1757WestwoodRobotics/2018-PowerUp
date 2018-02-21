package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import org.whsrobotics.subsystems.DriveTrain;
import org.whsrobotics.utils.RobotLogger;

public class Autonomous {

    // TODO: Convert to a singleton
    private static Autonomous instance;

    private static String gameData = "";
    private static DriverStation.Alliance alliance = DriverStation.Alliance.Invalid;

    public enum FieldTarget {
        CROSS_LINE, PC_ZONE, EXCHANGE,
        LEFT_SWITCH, RIGHT_SWITCH, LEFT_SCALE, RIGHT_SCALE;
    }

    public enum StartingPosition {
        LEFT, MIDDLE, RIGHT;
    }

    public static String getGameData() {
        if (gameData.isEmpty()) {
            try {
                gameData = DriverStation.getInstance().getGameSpecificMessage();
            } catch (Exception e) {
                RobotLogger.err(Autonomous.class, "Error with getting the Game Data! " + e.getMessage());   // TODO: singleton
            }
        }

        return gameData;
    }

    public static DriverStation.Alliance getAlliance() {
        if (alliance == DriverStation.Alliance.Invalid) {
            try {
                alliance = DriverStation.getInstance().getAlliance();
            } catch (Exception e) {
                RobotLogger.err(Autonomous.class, "Error with getting the Alliance Data! " + e.getMessage());   // TODO: Singleton
            }
        }

        return alliance;
    }
    public static Autonomous getInstance() {
        if (instance == null) {
            instance = new Autonomous();
        }
        return instance;
    }
}
