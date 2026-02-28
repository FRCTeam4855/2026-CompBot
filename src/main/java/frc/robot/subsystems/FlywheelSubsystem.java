package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.FlywheelConstants;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
    
    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkClosedLoopController m_PIDControllerL, m_PIDControllerM, m_PIDControllerR;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean flywheelRunning = false;
    public int goalFlywheelSpeed = 0;
    private SwerveSubsystem swerve;
    
    public FlywheelSubsystem() {
        m_flywheelL = new SparkFlex(FlywheelConstants.kFlywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkFlex(FlywheelConstants.kFlywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkFlex(FlywheelConstants.kFlywheelRCanId, MotorType.kBrushless); 
        m_PIDControllerL = m_flywheelL.getClosedLoopController();
        m_PIDControllerM = m_flywheelM.getClosedLoopController();
        m_PIDControllerR = m_flywheelR.getClosedLoopController();
        m_encoderL = m_flywheelL.getEncoder();
        m_encoderM = m_flywheelM.getEncoder();
        m_encoderR = m_flywheelR.getEncoder();


        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        int speedIndex = (int) Math.round(swerve.getDistanceToHub() * 4);
        goalFlywheelSpeed = swerve.getDistanceToHub() < 23 ? FlywheelConstants.kFlywheelSpeeds[speedIndex] : FlywheelConstants.kFlywheelSpeeds[23];
        SmartDashboard.putNumber("Goal Flywheel Speed", goalFlywheelSpeed);
    }

    public void toggleFlywheel() {
        if (flywheelRunning = false) {
            m_PIDControllerL.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            m_PIDControllerM.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            m_PIDControllerR.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            flywheelRunning = true;
        } else {
            m_flywheelL.set(0);
            m_flywheelM.set(0);
            m_flywheelR.set(0);
            flywheelRunning = false;
        }
    }
}