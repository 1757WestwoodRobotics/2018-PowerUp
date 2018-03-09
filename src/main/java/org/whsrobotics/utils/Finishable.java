package org.whsrobotics.utils;

/**
 * A simple FunctionalInterface that checks whether an implementation returns true or not
 */
@FunctionalInterface
public interface Finishable {

    /**
     *
     * @return true when the implementation is ready to stop a command.
     */
    boolean isFinished();

}
