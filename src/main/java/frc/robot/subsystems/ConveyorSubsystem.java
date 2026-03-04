package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs.ConveyorConfigs;
import frc.robot.Constants.ConveyorConstants;

public class ConveyorSubsystem extends SubsystemBase {
    
    public final SparkMax m_elevatorSpark, m_conveyorSpark;
    public final SparkClosedLoopController m_elevatorController, m_conveyorController;
    public boolean elevatorRunning, conveyorRunning = false;

    public ConveyorSubsystem() {
        m_elevatorSpark = new SparkMax(ConveyorConstants.kElevatorCanId, MotorType.kBrushless);
        m_conveyorSpark = new SparkMax(ConveyorConstants.kConveyorCanId, MotorType.kBrushless);

        m_elevatorController = m_elevatorSpark.getClosedLoopController();
        m_conveyorController = m_conveyorSpark.getClosedLoopController();

        m_elevatorSpark.configure(ConveyorConfigs.elevatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_conveyorSpark.configure(ConveyorConfigs.conveyorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void toggleSystem() {
        if (elevatorRunning || conveyorRunning) {
        m_conveyorSpark.set(0);
        m_conveyorSpark.set(0);
        elevatorRunning = false;
        conveyorRunning = false;
        } else {
        m_conveyorSpark.set(0.1);
        m_elevatorSpark.set(0.1);
        elevatorRunning = true;
        conveyorRunning = true;
        }
    }
}
