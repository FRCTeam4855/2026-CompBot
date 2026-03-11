package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs.FlywheelConfigs;
import frc.robot.Constants.FlywheelConstants;

public class IndexerSubsystem extends SubsystemBase {

    public final SparkMax m_indexer;
    public final SparkClosedLoopController m_indexerController;
    public boolean indexerRunning = false;

    public IndexerSubsystem() {
        m_indexer = new SparkMax(FlywheelConstants.kIndexerCanId, MotorType.kBrushless);
        m_indexerController = m_indexer.getClosedLoopController();
        m_indexer.configure(FlywheelConfigs.indexerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void toggleIndexer() {
        if (!indexerRunning) {
            m_indexerController.setSetpoint(FlywheelConstants.kIndexerSpeed, ControlType.kVelocity);
            indexerRunning = true;
        } else {
            m_indexer.set(0);
            indexerRunning = false;
        }
    }

    public void startIndexer() {
        m_indexerController.setSetpoint(FlywheelConstants.kIndexerSpeed, ControlType.kVelocity);
        indexerRunning = true;
    }

    public void stopIndexer() {
        m_indexer.set(0);
        indexerRunning = false;
    }
}
