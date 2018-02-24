package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.commands.commandgroups.*;
import org.whsrobotics.utils.RobotLogger;

/**
 * Code that runs at the beginning of Autonomous
 */
public class Autonomous {

    private static String gameData = "";
    private static FieldTarget fieldTarget = FieldTarget.CODE_DECISION;         // Default option
    private static StartingPosition startingPosition = StartingPosition.LEFT;   // Default option

    private static Command autonomousCommand;

    public enum FieldTarget {
        CODE_DECISION, NONE, CROSS_LINE, PC_ZONE, EXCHANGE,
        LEFT_SWITCH, RIGHT_SWITCH, LEFT_SCALE, RIGHT_SCALE;
    }

    public enum StartingPosition {
        LEFT, MIDDLE, RIGHT;
    }

    private enum FieldCombo {
        LSWITCH_LSCALE, LSWITCH_RSCALE, RSWITCH_LSCALE, RSWITCH_RSCALE;
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

    public static void startInit() {
        // get FMS switch/scale side data
        getGameData();

        // get Shuffleboard choosers
        fieldTarget = OI.getSelectedAutoFieldTarget();
        startingPosition = OI.getSelectedAutoStartingPosition();

        // get alliance
        switch (OI.getAlliance()) {
            case Red:
                break;
            case Blue:
                break;
            case Invalid:
                break;
        }
        // Set arduino light strips to alliance color (it should be white or alternating at this point)

        // choose commandgroup
        decideAutoMode().start();
    }

    private static String getGameData() {
        if (gameData.isEmpty()) {
            try {
                gameData = DriverStation.getInstance().getGameSpecificMessage();
            } catch (Exception e) {
                RobotLogger.err(instance.getClass(), "Error with getting the Game Data! " + e.getMessage());
            }
        }
        return gameData;
    }
//gamedata
    private static Command decideAutoMode() {

        if (fieldTarget == FieldTarget.NONE) {
            return new Command() {
                @Override
                protected boolean isFinished() {
                    return true;
                }
            };
        } else if (fieldTarget == FieldTarget.CODE_DECISION) {

            // TODO: Based on FMS data
            switch (startingPosition) {

                case LEFT:
                    break;
                case MIDDLE:
                    break;
                case RIGHT:
                    break;

            }

        } else {
            return manualAuto();
        }

    }

    private static Command manualAuto() {

        if (startingPosition == StartingPosition.LEFT) {

            switch (fieldTarget) {
                case CROSS_LINE:
                    return new AutoCrossLine();
                case PC_ZONE:
                    return new AutoLeftPCZone();
                case EXCHANGE:
                    return new AutoLeftExchange();
                case LEFT_SWITCH:
                    return new AutoLeftSwitch();
                case RIGHT_SWITCH:
                    return new AutoLeftToRightSwitch();
                case LEFT_SCALE:
                    return new AutoLeftScale();
                case RIGHT_SCALE:
                    return new AutoLeftToRightScale();
            }

        } else if (startingPosition == StartingPosition.MIDDLE) {

            switch (fieldTarget) {
                case CROSS_LINE:
                    return new AutoCrossLine();     // Maybe needs a specific Auto (else it crashes into PC Zone)
                case PC_ZONE:
                    return new AutoMiddlePCZone();
                case EXCHANGE:
                    return new AutoMiddleExchange();
                case LEFT_SWITCH:
                    return new AutoMiddleLeftSwitch();
                case RIGHT_SWITCH:
                    return new AutoMiddleRightSwitch();
            }

        } else if (startingPosition == StartingPosition.RIGHT) {

            switch (fieldTarget) {
                case CROSS_LINE:
                    return new AutoCrossLine();
                case PC_ZONE:
                    return new AutoRightPCZone();
                case LEFT_SWITCH:
                    return new AutoRightToLeftSwitch();
                case RIGHT_SWITCH:
                    return new AutoRightSwitch();
                case LEFT_SCALE:
                    return new AutoRightToLeftScale();
                case RIGHT_SCALE:
                    return new AutoRightScale();
            }

        }

        return new Command() {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };

    }

}
