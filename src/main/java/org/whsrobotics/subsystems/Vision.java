package org.whsrobotics.subsystems;

public class Vision {

    private static Vision instance;




    public static Vision getInstance(){

        if (instance == null) {
            instance = new Vision();

        }

        return instance;
    }



    // Jetson stuff

}