package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.Arduino;
import org.whsrobotics.subsystems.CubeSpinner;
import org.whsrobotics.utils.RobotLogger;

/**
 * Runs something until the Trigger is true
 */
public class FinishWithTriggerCommand extends Command {

    private Command command;
    private Trigger trigger;

    public FinishWithTriggerCommand(Command command, Trigger trigger) {
        this.command = command;
        this.trigger = trigger;
    }

    @Override
    protected void execute() {
        command.start();
    }

    @Override
    protected void end() {
        System.out.println("FinishWithTriggerCommand has stopped");
        (new SpinCubeSpinner(CubeSpinner.Mode.OFF)).start();
        command.cancel();
    }

    double lastTime = 0;
    double currentTime;

    @Override
    protected boolean isFinished() {

        currentTime = Timer.getFPGATimestamp();

        try {

            if (currentTime > lastTime + 3) {
                lastTime = currentTime;
                double output = Arduino.getInstance().getDistance();
                System.out.println(output);
                return output < 30;
            }

        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error with getting trigger for Wait", true);
            return false;
        }

        return false;

    }

}
