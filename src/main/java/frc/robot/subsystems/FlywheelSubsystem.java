package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.FlywheelConstants;
import frc.robot.Configs.FlywheelConfigs;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {
    
    public final SparkMax m_flywheelL, m_flywheelM, m_flywheelR;
    public final SparkClosedLoopController PIDControllerL, PIDControllerM, PIDControllerR;
    
    public FlywheelSubsystem() {
        m_flywheelL = new SparkMax(FlywheelConstants.kflywheelLCanId, MotorType.kBrushless); 
        m_flywheelM = new SparkMax(FlywheelConstants.kflywheelMCanId, MotorType.kBrushless); 
        m_flywheelR = new SparkMax(FlywheelConstants.kflywheelRCanId, MotorType.kBrushless); 
        PIDControllerL = m_flywheelL.getClosedLoopController();
        PIDControllerM = m_flywheelM.getClosedLoopController();
        PIDControllerR = m_flywheelR.getClosedLoopController();

        m_flywheelL.configure(FlywheelConfigs.flywheelConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
}