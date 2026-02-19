package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorSubsystem extends SubsystemBase {

    private static final SensorSubsystem instance = new SensorSubsystem();

    private static DigitalInput sensor1 = new DigitalInput(0);
    private static DigitalInput sensor2 = new DigitalInput(1);
    private static AnalogPotentiometer ultrasonicSensor = new AnalogPotentiometer(0);

    public SensorSubsystem() {
    }

    public static SensorSubsystem getInstance() {
        return instance;
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Sensor 1", getSensor1Value());
        SmartDashboard.putBoolean("Sensor 2", getSensor2Value());
        SmartDashboard.putNumber("Ultrasonic Sensor", getUltrasonicValue());
    }

    private boolean getSensor1Value() {
        return !sensor1.get();
    }

    private boolean getSensor2Value() {
        return !sensor2.get();
    }

    private double getUltrasonicValue() {
        return ultrasonicSensor.get();
    }

    public static boolean getSensor1() {
        return instance.getSensor1Value();
    }

    public static boolean getSensor2() {
        return instance.getSensor2Value();
    }

    public static double getUltrasonic() {
        return instance.getUltrasonicValue();
    }
}