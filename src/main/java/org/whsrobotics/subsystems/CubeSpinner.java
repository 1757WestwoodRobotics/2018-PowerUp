package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.whsrobotics.commands.SpinCubeSpinner;
import org.whsrobotics.robot.RobotMap;
import org.whsrobotics.utils.RobotLogger;

public class CubeSpinner extends Subsystem {

    private static TalonSRX left;
    private static TalonSRX right;

    public enum Mode {
        INWARDS(1), OUTWARDS(-1), OFF(0);

        private double speed;

        Mode(double speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private static CubeSpinner instance;

    private CubeSpinner() {

        try {
            left = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_LEFT.port);
            right = new TalonSRX(RobotMap.MotorControllerPort.SPINNER_RIGHT.port);

            left.setNeutralMode(NeutralMode.Coast);
            right.setNeutralMode(NeutralMode.Coast);

            left.configPeakOutputForward(0.5, 0);
            left.configPeakOutputReverse(-0.5, 0);

            left.setInverted(true);
            right.follow(left);

        } catch (Exception e) {
            RobotLogger.getInstance().err(this.getClass(), "Error instantiating CubeSpinner hardware" + e.getMessage(), true);

        }

    }

    public static CubeSpinner getInstance() {
        if (instance == null) {
            instance = new CubeSpinner();
        }

        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    public void periodic() {

    }

    public static void spinWithSpeed(double speed) {
        System.out.println(speed);
        left.set(ControlMode.PercentOutput, speed);

    }

    public static void spinWithMode(Mode mode){
        System.out.println(mode);
        spinWithSpeed(mode.getSpeed());
    }

//    // ------------ IR SENSOR METHODS ------------- //
//
//    /**
//     * Gets the Trigger object for the IR sensor
//     *
//     * @return irSensor trigger
//     */
//    public static Trigger getIRSensor() {
//        return irSensor;
//    }
//
//    /**
//     *
//     * @return raw analog value from the talon of the ir sensor
//     */
//    public static int getIRSensorValue() {
//        return left.getSensorCollection().getAnalogInRaw();
//    }
//
//    // IR Sensor Voltage Readings
//    // 41 = Cube
//    // 695 = No Cube
//    // 19 = Unplugged
//    // Convert to enum?
//
//    /**
//     * Returns true if the IR sensor analog reading is above the voltage when disconnected (~20)
//     * but below the voltage when it detects nothing (~690). May need to tune properly.
//     *
//     * @return true if IR sensor detects a cube in the arms
//     */
//    public static boolean isCubePresent() {
//        return getIRSensorValue() > 25 && getIRSensorValue() < 400;
//    }

}
