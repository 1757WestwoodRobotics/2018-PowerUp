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
    private static Ownership switchSide;
    private static Ownership scaleSide;
    private static FieldTarget fieldTarget = FieldTarget.CODE_DECISION;         // Default option
    private static StartingPosition startingPosition = StartingPosition.LEFT;   // Default option

    public enum FieldTarget {
        CODE_DECISION, NONE, CROSS_LINE, PC_ZONE, EXCHANGE,
        LEFT_SWITCH, RIGHT_SWITCH, LEFT_SCALE, RIGHT_SCALE;
    }

    public enum StartingPosition {
        LEFT, MIDDLE, RIGHT;
    }

    public enum Ownership {
        LEFT, RIGHT;
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

        // Try to get FMS switch/scale game data. If it fails, only cross the baseline and end.
        try {
            decodeGameData(getGameData());
        } catch (IllegalArgumentException e) {
            RobotLogger.getInstance().err(instance.getClass(), "Error with decoding game data!");
            new AutoCrossLine().start();    // queue AutoCrossLine
            return;                         // exit out of the method
        }

        // get Shuffleboard selections
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

        // choose manual commandgroup
        decideAutoMode().start();
    }

    private static String getGameData() {
        if (gameData.isEmpty()) {
            try {
                gameData = DriverStation.getInstance().getGameSpecificMessage();
            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error getting the Game Data! " + e.getMessage());
            }
        }
        return gameData;
    }

    private static void decodeGameData(String data){

        if (data.isEmpty() || data.length() != 3) {
            throw new IllegalArgumentException("Error with game data!");
        }

        // data.charAt(0) returns the side of the closest switch
        switch (data.charAt(0)) {
            case 'L':
                switchSide = Ownership.LEFT;
                break;
            case 'R':
                switchSide = Ownership.RIGHT;
                break;
        }

        // data.chatAt(1) returns the side of the scale
        switch (data.charAt(1)) {
            case 'L':
                scaleSide = Ownership.LEFT;
                break;
            case 'R':
                scaleSide = Ownership.RIGHT;
                break;
        }
        
    }



    private static Command decideAutoMode() {
        if (fieldTarget == FieldTarget.NONE) {
            return new Command() {
                @Override
                protected boolean isFinished() {
                    return true;
                }
            };
        } else if (fieldTarget == FieldTarget.CODE_DECISION) {
            // TODO: Make crossField param a SmartDashboard boolean?
            return new AutonomousCommand(startingPosition, switchSide, scaleSide, true);
        } else {
            return manualAuto();    // MIGHT NOT IMPLEMENT. ONLY IF DRIVER SELECTS SOMETHING OTHER THAN NONE/CODE_DEC
        }

    }

    // These commands start from the driver station wall TODO: REMOVE?
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

        } else {
            throw new IllegalStateException("No such StartingPosition or FieldTarget!");
        }

        return new Command() {
            @Override
            protected boolean isFinished() {
                return true;
            }
        };

    }

}
