package frc.robot.subsystems;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;

public class ClimberSubsystem extends SubsystemBase {
    
    public final SparkMax m_climberSpark;
    public final SparkClosedLoopController climberPIDController;

    public ClimberSubsystem() {
        m_climberSpark = new SparkMax(ClimberConstants.kClimberCanId, MotorType.kBrushless);
        climberPIDController = m_climberSpark.getClosedLoopController();
    }

    public void positionClimber(double position) {
        climberPIDController.setSetpoint(position, ControlType.kPosition);
    }
}
