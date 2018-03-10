package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.commands.DriveForward;
import org.whsrobotics.commands.commandgroups.*;
import org.whsrobotics.subsystems.Arduino;
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
        CODE_DECISION, NONE, CROSS_LINE
    }

    public enum StartingPosition {
        LEFT, MIDDLE, RIGHT
    }

    public enum Ownership {
        LEFT, RIGHT
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
            RobotLogger.getInstance().log(instance.getClass(), "## Game Data: " + getGameData());
        } catch (IllegalArgumentException e) {
            RobotLogger.getInstance().err(instance.getClass(), "Error with decoding game data!", false);
            new AutoCrossLine().start();    // queue AutoCrossLine
            return;                         // exit out of the method
        }

        // get Shuffleboard selections
        fieldTarget = OI.getSelectedAutoFieldTarget();
        startingPosition = OI.getSelectedAutoStartingPosition();

        // set arduino light strips to alliance color (it should be white or alternating at this point)
        Arduino.getInstance().Send(Arduino.Command.StripLEDs20vOff);
        RobotLogger.getInstance().log(instance.getClass(), "## Alliance: " + OI.getAlliance().toString());
        switch (OI.getAlliance()) {
            case Red:
                Arduino.getInstance().Send(Arduino.Command.StripLEDsRed);
                break;
            case Blue:
                Arduino.getInstance().Send(Arduino.Command.StripLEDsBlue);
                break;
            case Invalid:
                RobotLogger.getInstance().err(instance.getClass(), "Error with getting Alliance data!", false);
                break;
        }

        // choose manual commandgroup
        decideAutoMode().start();
    }

    private static String getGameData() {
        if (gameData.isEmpty()) {
            try {
                gameData = DriverStation.getInstance().getGameSpecificMessage();
            } catch (Exception e) {
                RobotLogger.getInstance().err(instance.getClass(), "Error getting the Game Data! " + e.getMessage(), false);
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
        } else if (fieldTarget == FieldTarget.CROSS_LINE) {
            return new DriveForward(5);
        } else {
            // Do not cross the field at the moment
            return new DriveForward(5);
            // return new AutonomousCommand(startingPosition, switchSide, scaleSide, false);
        }

    }

}
