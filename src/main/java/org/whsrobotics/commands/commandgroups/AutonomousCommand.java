package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.whsrobotics.commands.DriveForward;
import org.whsrobotics.commands.TurnToAngle;
import org.whsrobotics.robot.Autonomous;

public class AutonomousCommand extends CommandGroup {

    /**
     *
     * @param startingPosition
     * @param switchSide
     * @param scaleSide
     * @param crossField
     */
    public AutonomousCommand(Autonomous.StartingPosition startingPosition, Autonomous.Ownership switchSide, Autonomous.Ownership scaleSide, boolean crossField) {

        int numCubes = 1;

        // Left Routine
        if (startingPosition == Autonomous.StartingPosition.LEFT) {

            // DEPLOY CUBE IN LEFT SWITCH
            if (switchSide == Autonomous.Ownership.LEFT) {
                // Drive up to Switch (using Vision or encoder)
                addSequential(new DriveForward(5));  // TODO: Stop with sensor or time, or use Pathfinder
                // Deploy cube
                addSequential(new CGDeployCubeToSwitch()); // TODO: Stop with sensor
                numCubes--;
                // Drive to Point #2 (left 90 deg)
                addSequential(new TurnToAngle(-90));

            } else {
                // Drive to Point #2
                addSequential(new DriveForward(5));
            }

            if (scaleSide == Autonomous.Ownership.LEFT) {

                // Grab another cube if needed
                if (numCubes == 0) {
                    // Turn and align to grab cube
                    addSequential(new TurnToAngle(10)); // TODO: Calculate
                    addSequential(new DriveForward(5));  // TODO: Drive with Vision
                    addSequential(new CGGrabCube());
                    numCubes++;

                    // Reorient to scale
                    addSequential(new TurnToAngle(0));    // TODO: Calculate
                    addSequential(new DriveForward(5));
                } else {
                    // Orient to scale
                    addSequential(new TurnToAngle(0));
                    addSequential(new DriveForward(5));
                }

                // Deploy the cube
                addSequential(new CGDeployCubeToScale());
                numCubes--;

                // Return to Point #2 (90 deg)
                //  Backup
                //  TurnToAngle(90)

            }

            if (switchSide == Autonomous.Ownership.LEFT) {
                // Turn around
                // Grab another cube
                numCubes++;
                // Deploy the cube
            }

//            // Only if we choose to cross the field AND the field has randomized everything to the right
//            if (crossField && (switchSide == Autonomous.Ownership.RIGHT || scaleSide == Autonomous.Ownership.RIGHT)) {
//
//                // Drive to the other side of the field
//                addSequential(new DriveForward());
//
//                if (numCubes == 0) {
//                    // Grab a cube (Turn, Vision, Drive)
//                }
//
//                if (switchSide == Autonomous.Ownership.RIGHT) {
//                    // Drop in switch [Turn, Drive]
//                    addSequential(new CGDeployCubeToSwitch()); // NOT VISION BASED (NO VISION TARGETS)
//                    numCubes--;
//                }
//
//                if (scaleSide == Autonomous.Ownership.RIGHT) {
//                    // Drop in scale [Turn, Drive]
//                    addSequential(new CGDeployCubeToScale());
//                    numCubes--;
//                }
//
//            }
//
//        // If robot starts on the right side of the field
//        } else if (startingPosition == Autonomous.StartingPosition.RIGHT) {
//
//            // TODO: IMPLEMENT!!!!
//
//        // If robot starts in the middle of the field
//        } else if (startingPosition == Autonomous.StartingPosition.MIDDLE) {
//
//            // Drive forwards a little
//            addSequential(new DriveForward());  // TODO: Sensor/Time based
//
//            // Turns either ## degrees LEFT or RIGHT depending on switchSide
//            if (switchSide == Autonomous.Ownership.LEFT) {
//                addSequential(new TurnToAngle(-15));
//            } else {
//                addSequential(new TurnToAngle(15));
//            }
//
//            // Drive towards reflective target
//
//            // Deploy cube
//            addSequential(new CGDeployCubeToSwitch());
//
//            // Turn accordingly towards PC Zone
//            // Grab another cube
//            // Throw it in the proper switch
//            // Grab another cube?

        } else {
            throw new IllegalStateException("Error with autonomous decision!");
        }

    }

}
