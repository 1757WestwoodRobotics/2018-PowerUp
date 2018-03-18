package org.whsrobotics.utils;

/**
 * An enum to simplify the conversion of real-world units in the program.
 *
 * @author Larry Tseng, Sean Lendrum, Michael Serratore
 */
public enum Units {
    NATIVE_CTRE_MAGENC, NATIVE_QUAD_ENC, INCHES, FEET, CENTIMETERS, METERS, DEGREES, RADIANS;

    /**
     *
     * @param from the unit to convert from
     * @param to the unit to convert to
     * @param value the value of the 'from' unit to be converted
     * @return converted value in the units of 'to'
     * @throws IllegalArgumentException if the conversion is not yet implemented or possible
     */
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
                            return value * 100.0;
                        case INCHES:
                            return value * 39.37;
                    }
                    break;

                case CENTIMETERS:
                    switch (to) {
                        case INCHES:
                            return value * 0.39;
                        case FEET:
                            return value * 0.033;
                        case METERS:
                            return value * 0.01;
                    }
                    break;

                case DEGREES:
                    switch (to) {
                        case RADIANS:
                            return Math.toRadians(value);
                        case NATIVE_CTRE_MAGENC:
                            return (value / 360) * 4096;
                        case NATIVE_QUAD_ENC:
                            return (value / 360) * 2048;
                    }
                    break;

                case RADIANS:
                    switch (to) {
                        case DEGREES:
                            return Math.toDegrees(value);
                        case NATIVE_CTRE_MAGENC:
                            return value / (2 * Math.PI) * 4096;
                        case NATIVE_QUAD_ENC:
                            return value / (2 * Math.PI) * 2048;
                    }
                    break;

                // 4096 resolution
                case NATIVE_CTRE_MAGENC:
                    switch (to) {
                        case DEGREES:
                            return (value / 4096) * 360;
                        case RADIANS:
                            return (value / 4096) * 2 * Math.PI;
                    }
                    break;

                    // 2048 resolution
                case NATIVE_QUAD_ENC:
                    switch (to) {
                        case DEGREES:
                            return (value / 2048) * 360;
                        case RADIANS:
                            return (value / 2048) * 2 * Math.PI;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Cannot convert between these units!");
            }
        }

        return value;

    }

}
