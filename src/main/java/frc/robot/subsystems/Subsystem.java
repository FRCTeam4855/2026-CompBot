package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Subsystem extends SubsystemBase {
    public abstract void robotInit();
    public abstract void teleopInit();
    public abstract void autonomousInit();
}
