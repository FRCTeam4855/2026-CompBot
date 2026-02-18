package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.FlywheelConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Flywheel extends SubsystemBase {
    
    public final SparkMax m_flywheel1, m_flywheel2, m_flywheel3;
    public final SparkClosedLoopController PIDController1, PIDController2, PIDController3;
    
    public Flywheel() {
        m_flywheel1 = new SparkMax(FlywheelConstants.kflywheel1CanId, MotorType.kBrushless);
        m_flywheel2 = new SparkMax(FlywheelConstants.kflywheel2CanId, MotorType.kBrushless);
        m_flywheel3 = new SparkMax(FlywheelConstants.kflywheel3CanId, MotorType.kBrushless);
        PIDController1 = m_flywheel1.getClosedLoopController();
        PIDController2 = m_flywheel2.getClosedLoopController();
        PIDController3 = m_flywheel3.getClosedLoopController();

        m_flywheel1.configure(FlywheelConfigs)
    }
}
