package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs.ConveyorConfigs;
import frc.robot.Constants.ConveyorConstants;

public class ConveyorSubsystem extends SubsystemBase {
    
    public final SparkMax m_elevatorSpark, m_conveyorSpark;
    public final SparkClosedLoopController m_elevatorController, m_conveyorController;
    public boolean elevatorRunning = false, conveyorRunning = false;

    public ConveyorSubsystem() {
        m_elevatorSpark = new SparkMax(ConveyorConstants.kElevatorCanId, MotorType.kBrushless);
        m_conveyorSpark = new SparkMax(ConveyorConstants.kConveyorCanId, MotorType.kBrushless);

        m_elevatorController = m_elevatorSpark.getClosedLoopController();
        m_conveyorController = m_conveyorSpark.getClosedLoopController();

        m_elevatorSpark.configure(ConveyorConfigs.elevatorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        m_conveyorSpark.configure(ConveyorConfigs.conveyorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void toggleConveyor() {
        if (elevatorRunning || conveyorRunning) {
            m_conveyorController.setSetpoint(0.0, ControlType.kVelocity);
            m_elevatorController.setSetpoint(0.0, ControlType.kVelocity);
            elevatorRunning = false;
            conveyorRunning = false;
        } else {
            m_conveyorController.setSetpoint(ConveyorConstants.kConveyorSpeed, ControlType.kVelocity);
            m_elevatorController.setSetpoint(ConveyorConstants.kElevatorSpeed, ControlType.kVelocity);
            elevatorRunning = true;
            conveyorRunning = true;
        }
    }
}
