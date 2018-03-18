package org.whsrobotics.utils;

public enum Units {
    NATIVE_CTRE_MAGENC, NATIVE_QUAD_ENC, INCHES, FEET, CENTIMETERS, METERS, DEGREES, RADIANS;

    public static double convert(Units from, Units to, double value) throws IllegalArgumentException {

        if (from != to) {

            switch (from) {
                case FEET:
                    switch (to) {
                        case INCHES:
                            return value * 12.0;
                        case CENTIMETERS:
                            return value * 30.48;
                        case METERS:
                            return value * 0.3048;
                    }
                    break;

                case INCHES:
                    switch (to) {
                        case FEET:
                            return value * 0.0833;
                        case METERS:
                            return value * 0.0254;
                        case CENTIMETERS:
                            return value * 2.54;
                    }
                    break;

                case METERS:
                    switch (to) {
                        case FEET:
                            return value * 3.281;
                        case CENTIMETERS:
                            return value * 100;
                        case INCHES:
                            return value * 39.37;
                    }
                    break;

                case CENTIMETERS:
                    switch (to) {
                        case INCHES:

                    }

                case DEGREES:

                case RADIANS:


                default:
                    throw new IllegalArgumentException("Cannot convert between these units!");
            }
        }

        return value;

    }

}

