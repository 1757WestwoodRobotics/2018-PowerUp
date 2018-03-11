package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.Arduino;
import org.whsrobotics.utils.RobotLogger;

import java.util.concurrent.atomic.AtomicReference;

public class ArduinoUltrasonicDistance extends Command {

//    private AtomicReference<Double> output = new AtomicReference<>((double) 100);
//    private Thread thread;
    private double value;   // the value

    public ArduinoUltrasonicDistance(double value) {
        this.value = value;
    }

    @Override
    protected boolean isFinished() {

//        // If the thread is not alive, create a new thread and start it
//        if (!thread.isAlive() || thread == null) {
//            thread = new Thread(() -> {
//                // lambda implements the Runnable.run() method
//                output.set(Arduino.getInstance().getDistance());
//                System.out.println("Ultrasonic reading: " + output.get());
//            });
//            thread.start();
//        }
//
//        // returns true (effectively finishing the command) when output is less than value (but not -1)
//        return output.get() < value && output.get() != -1;

        double output = Arduino.getInstance().getDistance(); // --> replace ArduinoUltrasonicCommand
        return output < value && output != -1;

    }

    @Override
    protected void end() {
//        try {
//            thread.interrupt();
//        } catch (Exception e) {
//            RobotLogger.getInstance().err(this.getClass(), "Error interrupting ArduinoUltrasonicDistance thread!", true);
//        }
    }
}
