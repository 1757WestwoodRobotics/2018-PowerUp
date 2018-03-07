package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.whsrobotics.subsystems.DriveTrain;

public class DrivePathFollowing extends Command {

    private Waypoint[] waypoints;
    private Trajectory trajectory;

    public DrivePathFollowing(Waypoint[] waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    protected void initialize() {
        DriveTrain.generateTrajectory(waypoints);
    }

    @Override
    protected void execute() {
        DriveTrain.applyTrajectory(trajectory);
    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
