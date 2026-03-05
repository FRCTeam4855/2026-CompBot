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
import frc.robot.RobotContainer;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
    
    public final SparkFlex m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkMax m_indexer;
    public final SparkClosedLoopController m_pidControllerL, m_pidControllerM, m_pidControllerR, m_indexerController;
    public final RelativeEncoder m_encoderL, m_encoderM, m_encoderR;
    public boolean flywheelRunning = false, flywheelSpeedSet = false, indexerRunning = false;
    public int goalFlywheelSpeed = 0;
    private SwerveSubsystem swerve = RobotContainer.drivebase;

    public FlywheelSubsystem() {
        m_flywheelL = new SparkFlex(FlywheelConstants.kFlywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkFlex(FlywheelConstants.kFlywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkFlex(FlywheelConstants.kFlywheelRCanId, MotorType.kBrushless);
        m_indexer = new SparkMax(FlywheelConstants.kIndexerCanId, MotorType.kBrushless);
        m_pidControllerL = m_flywheelL.getClosedLoopController();
        m_pidControllerM = m_flywheelM.getClosedLoopController();
        m_pidControllerR = m_flywheelR.getClosedLoopController();
        m_indexerController = m_indexer.getClosedLoopController();
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
        SmartDashboard.putNumber("Current Flywheel Speed", m_encoderL.getVelocity());
    }

    public void stopFlywheel() {
        m_flywheelL.set(0);
        m_flywheelM.set(0);
        m_flywheelR.set(0);
    }

    public void toggleIndexer() {
        if (!indexerRunning) {
            m_indexerController.setSetpoint(FlywheelConstants.kIndexerSpeed, ControlType.kVelocity);
            indexerRunning = true;
        } else {
            m_indexerController.setSetpoint(0, ControlType.kVelocity);
            indexerRunning = false;
        }
    }
}