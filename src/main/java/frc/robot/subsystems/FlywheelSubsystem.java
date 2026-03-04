package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.FlywheelConstants;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
    
    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkMax m_indexer;
    public final SparkClosedLoopController m_pidControllerL, m_pidControllerM, m_pidControllerR, m_indexerPidController;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean flywheelRunning = false;
    public boolean indexerRunning = false;
    public int goalFlywheelSpeed = 0;
    private SwerveSubsystem swerve;
    
    public FlywheelSubsystem() {
        m_flywheelL = new SparkFlex(FlywheelConstants.kFlywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkFlex(FlywheelConstants.kFlywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkFlex(FlywheelConstants.kFlywheelRCanId, MotorType.kBrushless);
        m_indexer = new SparkMax(FlywheelConstants.kIndexerCanId, MotorType.kBrushless);
        m_pidControllerL = m_flywheelL.getClosedLoopController();
        m_pidControllerM = m_flywheelM.getClosedLoopController();
        m_pidControllerR = m_flywheelR.getClosedLoopController();
        m_indexerPidController = m_indexer.getClosedLoopController();
        m_encoderL = m_flywheelL.getEncoder();
        m_encoderM = m_flywheelM.getEncoder();
        m_encoderR = m_flywheelR.getEncoder();


        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_flywheelM.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_flywheelR.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_indexer.configure(FlywheelConfigs.indexerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        int speedIndex = (int) Math.round(swerve.getDistanceToHub() * 4);
        goalFlywheelSpeed = swerve.getDistanceToHub() < 23 ? FlywheelConstants.kFlywheelSpeeds[speedIndex] : FlywheelConstants.kFlywheelSpeeds[23];
        SmartDashboard.putNumber("Goal Flywheel Speed", goalFlywheelSpeed);
    }

    public void toggleFlywheel() {
        if (!flywheelRunning) {
            m_pidControllerL.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            m_pidControllerM.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            m_pidControllerR.setSetpoint(goalFlywheelSpeed, ControlType.kVelocity);
            flywheelRunning = true;
        } else {
            m_flywheelL.set(0);
            m_flywheelM.set(0);
            m_flywheelR.set(0);
            flywheelRunning = false;
        }
    }

    public void toggleIndexer() {
        if (!indexerRunning) {
            m_indexer.set(0.1);
            indexerRunning = true;
        } else {
            m_indexer.set(0);
            indexerRunning = false;
        }
    }
}